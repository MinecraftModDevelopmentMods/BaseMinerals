package zone.moddev.mc.baseminerals.migration;

import net.minecraft.util.datafix.DefaultTypeReferences;
import com.mojang.datafixers.DataFixer;
import com.mojang.datafixers.Dynamic;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import net.minecraft.block.Block;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.datafix.fixes.BlockStateFlatteningMap;
import net.minecraft.world.storage.SaveHandler;
import net.minecraftforge.registries.ForgeRegistries;

/** Supplies only this world's mineral states during the numeric chunk conversion. */
public final class LegacyWorldUpgrade {
    private static final Logger LOGGER = LogManager.getLogger("baseminerals");
    private static final String SNAPSHOT = "baseminerals-legacy-registry.dat";
    private static final String PRESERVE = "BaseMineralsLegacyPreserveChunk";
    private static final ThreadLocal<Map<Integer, Dynamic<?>[]>> ACTIVE = new ThreadLocal<>();
    private static final Map<Path, Map<Integer, Dynamic<?>[]>> WORLDS = new ConcurrentHashMap<>();
    private static final Map<Path, ListNBT> DISCOVERED = new ConcurrentHashMap<>();

    private LegacyWorldUpgrade() {}

    public static void prepareWorld(CompoundNBT root, File file, SaveHandler handler) {
        migrateItems(root.getCompound("Data").getCompound("Player"));
        if (Boolean.getBoolean("baseminerals.debugUpgrade")) {
            LOGGER.info("World read {} active={} dataVersion={} root={}", file, handler != null,
                    root.getCompound("Data").getInt("DataVersion"), root.keySet());
        }
        ListNBT minerals = mineralIds(root);
        if (minerals.isEmpty()) return;
        try {
            Path directory = file.getCanonicalFile().getParentFile().toPath();
            // Force Upgrade reads WorldInfo without a SaveHandler and can rewrite level.dat
            // before the upgrade thread sees its first chunk. Remember the original read.
            DISCOVERED.put(directory, minerals);
            if (handler != null) persistRegistry(directory, minerals);
        } catch (IOException e) {
            throw new IllegalStateException("Could not preserve the old Base Minerals registry from " + file, e);
        }
    }

    private static ListNBT mineralIds(CompoundNBT root) {
        ListNBT ids = root.getCompound("FML").getCompound("Registries")
                .getCompound("minecraft:blocks").getList("ids", 10);
        ListNBT minerals = new ListNBT();
        for (int i = 0; i < ids.size(); i++) {
            CompoundNBT id = ids.getCompound(i);
            if (id.getString("K").startsWith("baseminerals:")) minerals.add(id.copy());
        }
        if (Boolean.getBoolean("baseminerals.debugUpgrade")) {
            LOGGER.info("Registry scan {} block IDs, {} mineral IDs (root {})", ids.size(), minerals.size(), root.keySet());
        }
        return minerals;
    }

    private static synchronized Map<Integer, Dynamic<?>[]> persistRegistry(Path directory, ListNBT minerals)
            throws IOException {
        Path snapshot = directory.resolve(SNAPSHOT);
        CompoundNBT registry = new CompoundNBT();
        registry.put("Blocks", minerals);
        Map<Integer, Dynamic<?>[]> states = bake(minerals);
        if (Files.isRegularFile(snapshot)) {
            if (!registry.equals(CompressedStreamTools.readCompressed(Files.newInputStream(snapshot)))) {
                throw new IllegalStateException("Conflicting Base Minerals registry in " + directory);
            }
        } else {
            Path temporary = Files.createTempFile(directory, "baseminerals-registry-", ".tmp");
            try {
                CompressedStreamTools.writeCompressed(registry, Files.newOutputStream(temporary));
                Files.move(temporary, snapshot, StandardCopyOption.ATOMIC_MOVE);
            } finally {
                Files.deleteIfExists(temporary);
            }
            LOGGER.info("Saved {} legacy Base Minerals block IDs in {}", minerals.size(), snapshot);
        }
        WORLDS.put(directory, states);
        return states;
    }

    public static CompoundNBT fixChunk(DataFixer fixer, DefaultTypeReferences type, CompoundNBT chunk,
            int fromVersion, int toVersion, File regionDirectory) {
        migrateItems(chunk.getCompound("Level"));
        Map<Integer, Dynamic<?>[]> states = statesFor(regionDirectory);
        if (states.isEmpty()) return NBTUtil.update(fixer, type, chunk, fromVersion, toVersion);
        CompoundNBT level = chunk.getCompound("Level");
        level.putBoolean("TerrainPopulated", true);
        level.putBoolean("LightPopulated", true);
        level.putBoolean(PRESERVE, true);
        Map<Integer, Dynamic<?>[]> previous = ACTIVE.get();
        ACTIVE.set(states);
        try {
            return NBTUtil.update(fixer, type, chunk, fromVersion, toVersion);
        } finally {
            if (previous == null) ACTIVE.remove();
            else ACTIVE.set(previous);
        }
    }

    public static CompoundNBT finishChunk(DataFixer fixer, DefaultTypeReferences type, CompoundNBT chunk, int version) {
        CompoundNBT fixed = NBTUtil.update(fixer, type, chunk, version);
        CompoundNBT level = fixed.getCompound("Level");
        if (level.getBoolean(PRESERVE)) {
            level.putString("Status", "full");
            level.remove(PRESERVE);
        }
        return fixed;
    }

    public static Dynamic<?> mineralState(int packedId) {
        Map<Integer, Dynamic<?>[]> states = ACTIVE.get();
        if (states == null) return null;
        Dynamic<?>[] entries = states.get(packedId >>> 4);
        return entries == null ? null : entries[packedId & 15];
    }

    private static Map<Integer, Dynamic<?>[]> statesFor(File chunkDirectory) {
        try {
            for (Path directory = chunkDirectory.getCanonicalFile().toPath(); directory != null;
                    directory = directory.getParent()) {
                Map<Integer, Dynamic<?>[]> known = WORLDS.get(directory);
                if (known != null) return known;
                Path snapshot = directory.resolve(SNAPSHOT);
                if (Files.isRegularFile(snapshot)) {
                    return WORLDS.computeIfAbsent(directory, ignored -> {
                        try {
                            CompoundNBT data = CompressedStreamTools.readCompressed(Files.newInputStream(snapshot));
                            return bake(data.getList("Blocks", 10));
                        } catch (IOException e) {
                            throw new IllegalStateException("Could not read " + snapshot, e);
                        }
                    });
                }
                Path level = directory.resolve("level.dat");
                if (Files.isRegularFile(level)) {
                    ListNBT minerals = DISCOVERED.get(directory);
                    if (minerals == null) {
                        minerals = mineralIds(CompressedStreamTools.readCompressed(Files.newInputStream(level)));
                    }
                    if (!minerals.isEmpty()) return persistRegistry(directory, minerals);
                    return WORLDS.computeIfAbsent(directory, ignored -> Collections.emptyMap());
                }
            }
            return Collections.emptyMap();
        } catch (IOException e) {
            throw new IllegalStateException("Could not locate the world for " + chunkDirectory, e);
        }
    }

    private static Map<Integer, Dynamic<?>[]> bake(ListNBT ids) {
        Map<Integer, Dynamic<?>[]> result = new HashMap<>();
        for (int i = 0; i < ids.size(); i++) {
            CompoundNBT id = ids.getCompound(i);
            ResourceLocation name = new ResourceLocation(id.getString("K"));
            int numeric = id.getInt("V");
            if (!"baseminerals".equals(name.getNamespace()) || numeric < 1 || numeric > 4095
                    || result.containsKey(numeric) || !ForgeRegistries.BLOCKS.containsKey(name)) {
                throw new IllegalStateException("No unambiguous Base Minerals state for " + name + " (" + numeric + ")");
            }
            Block block = ForgeRegistries.BLOCKS.getValue(name);
            Dynamic<?>[] entries = new Dynamic<?>[16];
            for (int meta = 0; meta < entries.length; meta++) {
                BlockState state = block.getDefaultState();
                if (block instanceof FlowingFluidBlock) state = state.with(FlowingFluidBlock.LEVEL, meta);
                entries[meta] = BlockStateFlatteningMap.makeDynamic(NBTUtil.writeBlockState(state).toString());
            }
            result.put(numeric, entries);
        }
        return Collections.unmodifiableMap(result);
    }

    public static void migrateItems(INBT value) {
        if (value instanceof CompoundNBT) {
            CompoundNBT compound = (CompoundNBT) value;
            if ("forge:bucketfilled".equals(compound.getString("id")) && compound.contains("Count", 99)
                    && compound.contains("tag", 10)) {
                CompoundNBT tag = compound.getCompound("tag");
                String fluid = tag.getString("FluidName");
                if (fluid.startsWith("baseminerals:")) fluid = fluid.substring("baseminerals:".length());
                if ("lithium".equals(fluid) || "silicon".equals(fluid)) {
                    compound.putString("id", "baseminerals:" + fluid + "_bucket");
                    tag.remove("FluidName");
                    tag.remove("Amount");
                    if (tag.isEmpty()) compound.remove("tag");
                }
            }
            for (String key : new ArrayList<>(compound.keySet())) migrateItems(compound.get(key));
        } else if (value instanceof ListNBT) {
            ListNBT list = (ListNBT) value;
            for (int i = 0; i < list.size(); i++) migrateItems(list.get(i));
        }
    }
}
