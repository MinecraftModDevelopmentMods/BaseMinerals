package zone.moddev.mc.baseminerals.migration;

import com.mojang.datafixers.DSL.TypeReference;
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
import net.minecraft.block.BlockFlowingFluid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.INBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.datafix.fixes.BlockStateFlatteningMap;
import net.minecraft.world.chunk.storage.AnvilChunkLoader;
import net.minecraft.world.storage.SaveHandler;
import net.minecraftforge.registries.ForgeRegistries;

/** Supplies only this world's mineral states during the numeric chunk conversion. */
public final class LegacyWorldUpgrade {
    private static final Logger LOGGER = LogManager.getLogger("baseminerals");
    private static final String SNAPSHOT = "baseminerals-legacy-registry.dat";
    private static final String PRESERVE = "BaseMineralsLegacyPreserveChunk";
    private static final ThreadLocal<Map<Integer, Dynamic<?>[]>> ACTIVE = new ThreadLocal<>();
    private static final Map<Path, Map<Integer, Dynamic<?>[]>> WORLDS = new ConcurrentHashMap<>();
    private static final Map<Path, NBTTagList> DISCOVERED = new ConcurrentHashMap<>();

    private LegacyWorldUpgrade() {}

    public static void prepareWorld(NBTTagCompound root, File file, SaveHandler handler) {
        migrateItems(root.getCompound("Data").getCompound("Player"));
        if (Boolean.getBoolean("baseminerals.debugUpgrade")) {
            LOGGER.info("World read {} active={} dataVersion={} root={}", file, handler != null,
                    root.getCompound("Data").getInt("DataVersion"), root.keySet());
        }
        NBTTagList minerals = mineralIds(root);
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

    private static NBTTagList mineralIds(NBTTagCompound root) {
        NBTTagList ids = root.getCompound("FML").getCompound("Registries")
                .getCompound("minecraft:blocks").getList("ids", 10);
        NBTTagList minerals = new NBTTagList();
        for (int i = 0; i < ids.size(); i++) {
            NBTTagCompound id = ids.getCompound(i);
            if (id.getString("K").startsWith("baseminerals:")) minerals.add(id.copy());
        }
        if (Boolean.getBoolean("baseminerals.debugUpgrade")) {
            LOGGER.info("Registry scan {} block IDs, {} mineral IDs (root {})", ids.size(), minerals.size(), root.keySet());
        }
        return minerals;
    }

    private static synchronized Map<Integer, Dynamic<?>[]> persistRegistry(Path directory, NBTTagList minerals)
            throws IOException {
        Path snapshot = directory.resolve(SNAPSHOT);
        NBTTagCompound registry = new NBTTagCompound();
        registry.setTag("Blocks", minerals);
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

    public static NBTTagCompound fixChunk(DataFixer fixer, TypeReference type, NBTTagCompound chunk,
            int fromVersion, int toVersion, AnvilChunkLoader loader) {
        migrateItems(chunk.getCompound("Level"));
        Map<Integer, Dynamic<?>[]> states = statesFor(loader.chunkSaveLocation);
        if (states.isEmpty()) return NBTUtil.update(fixer, type, chunk, fromVersion, toVersion);
        NBTTagCompound level = chunk.getCompound("Level");
        level.setBoolean("TerrainPopulated", true);
        level.setBoolean("LightPopulated", true);
        level.setBoolean(PRESERVE, true);
        Map<Integer, Dynamic<?>[]> previous = ACTIVE.get();
        ACTIVE.set(states);
        try {
            return NBTUtil.update(fixer, type, chunk, fromVersion, toVersion);
        } finally {
            if (previous == null) ACTIVE.remove();
            else ACTIVE.set(previous);
        }
    }

    public static NBTTagCompound finishChunk(DataFixer fixer, TypeReference type, NBTTagCompound chunk, int version) {
        NBTTagCompound fixed = NBTUtil.update(fixer, type, chunk, version);
        NBTTagCompound level = fixed.getCompound("Level");
        if (level.getBoolean(PRESERVE)) {
            level.setString("Status", "postprocessed");
            level.removeTag(PRESERVE);
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
                            NBTTagCompound data = CompressedStreamTools.readCompressed(Files.newInputStream(snapshot));
                            return bake(data.getList("Blocks", 10));
                        } catch (IOException e) {
                            throw new IllegalStateException("Could not read " + snapshot, e);
                        }
                    });
                }
                Path level = directory.resolve("level.dat");
                if (Files.isRegularFile(level)) {
                    NBTTagList minerals = DISCOVERED.get(directory);
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

    private static Map<Integer, Dynamic<?>[]> bake(NBTTagList ids) {
        Map<Integer, Dynamic<?>[]> result = new HashMap<>();
        for (int i = 0; i < ids.size(); i++) {
            NBTTagCompound id = ids.getCompound(i);
            ResourceLocation name = new ResourceLocation(id.getString("K"));
            int numeric = id.getInt("V");
            if (!"baseminerals".equals(name.getNamespace()) || numeric < 1 || numeric > 4095
                    || result.containsKey(numeric) || !ForgeRegistries.BLOCKS.containsKey(name)) {
                throw new IllegalStateException("No unambiguous Base Minerals state for " + name + " (" + numeric + ")");
            }
            Block block = ForgeRegistries.BLOCKS.getValue(name);
            Dynamic<?>[] entries = new Dynamic<?>[16];
            for (int meta = 0; meta < entries.length; meta++) {
                IBlockState state = block.getDefaultState();
                if (block instanceof BlockFlowingFluid) state = state.with(BlockFlowingFluid.LEVEL, meta);
                entries[meta] = BlockStateFlatteningMap.makeDynamic(NBTUtil.writeBlockState(state).toString());
            }
            result.put(numeric, entries);
        }
        return Collections.unmodifiableMap(result);
    }

    public static void migrateItems(INBTBase value) {
        if (value instanceof NBTTagCompound) {
            NBTTagCompound compound = (NBTTagCompound) value;
            if ("forge:bucketfilled".equals(compound.getString("id")) && compound.contains("Count", 99)
                    && compound.contains("tag", 10)) {
                NBTTagCompound tag = compound.getCompound("tag");
                String fluid = tag.getString("FluidName");
                if (fluid.startsWith("baseminerals:")) fluid = fluid.substring("baseminerals:".length());
                if ("lithium".equals(fluid) || "silicon".equals(fluid)) {
                    compound.setString("id", "baseminerals:" + fluid + "_bucket");
                    tag.removeTag("FluidName");
                    tag.removeTag("Amount");
                    if (tag.isEmpty()) compound.removeTag("tag");
                }
            }
            for (String key : new ArrayList<>(compound.keySet())) migrateItems(compound.getTag(key));
        } else if (value instanceof NBTTagList) {
            NBTTagList list = (NBTTagList) value;
            for (int i = 0; i < list.size(); i++) migrateItems(list.get(i));
        }
    }
}
