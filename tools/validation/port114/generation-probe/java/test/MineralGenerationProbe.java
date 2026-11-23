package test;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.util.Map;
import java.util.TreeMap;
import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.server.FMLServerStartedEvent;
import net.minecraftforge.registries.ForgeRegistries;

/** Disposable full-height census; contains no placement implementation. */
@Mod("mineralgenerationprobe")
public final class MineralGenerationProbe {
    private static final String[] ORES = {"lithium", "niter", "salt", "sulfur", "phosphorus", "potash", "saltpeter"};
    private static final int[][] HEIGHTS = {{8,47}, {16,63}, {24,79}, {4,31}, {16,63}, {8,47}, {24,79}};
    public MineralGenerationProbe() { MinecraftForge.EVENT_BUS.addListener(this::started); }

    private void started(FMLServerStartedEvent event) {
        JsonObject report = new JsonObject();
        long start = System.nanoTime();
        try {
            report.addProperty("seed", event.getServer().getWorld(DimensionType.OVERWORLD).getSeed());
            report.addProperty("interior_chunks_per_dimension", 256);
            report.addProperty("margin_chunks", 2);
            report.addProperty("min_y", 0); report.addProperty("max_y", 255);
            JsonObject dimensions = new JsonObject();
            for (DimensionType type : new DimensionType[] {DimensionType.OVERWORLD, DimensionType.THE_NETHER, DimensionType.THE_END}) {
                ServerWorld world = event.getServer().getWorld(type);
                long begin = System.nanoTime();
                for (int x = 62; x < 82; x++) for (int z = 62; z < 82; z++) world.getChunk(x, z);
                JsonObject before = census(world, type == DimensionType.OVERWORLD);
                // Generate later chunks beyond the populated margin, then census the old interior again.
                for (int z = 62; z < 82; z++) world.getChunk(82, z);
                JsonObject after = census(world, type == DimensionType.OVERWORLD);
                if (!before.equals(after)) throw new AssertionError("Later generation changed existing ores in " + type);
                before.addProperty("populate_and_census_seconds", (System.nanoTime() - begin) / 1e9);
                dimensions.add(DimensionType.getKey(type).toString(), before);
                System.out.println("MINERAL_GENERATION_DIMENSION " + type + " " + before);
            }
            report.add("dimensions", dimensions);
            report.addProperty("passed", true);
        } catch (Throwable failure) {
            report.addProperty("passed", false);
            report.addProperty("failure", failure.toString());
            failure.printStackTrace();
        } finally {
            report.addProperty("seconds", (System.nanoTime() - start) / 1e9);
            try {
                Files.write(Paths.get("generation-results.json"), new GsonBuilder().setPrettyPrinting().create()
                        .toJson(report).getBytes(StandardCharsets.UTF_8));
            } catch (java.io.IOException error) { throw new IllegalStateException(error); }
            System.out.println("MINERAL_GENERATION_PROBE " + (report.get("passed").getAsBoolean() ? "PASS" : "FAIL"));
            event.getServer().initiateShutdown(false);
        }
    }

    private static JsonObject census(ServerWorld world, boolean overworld) throws Exception {
        Map<Block, Integer> indices = new java.util.IdentityHashMap<>();
        int[][] histogram = new int[7][256];
        for (int i = 0; i < ORES.length; i++) indices.put(ForgeRegistries.BLOCKS.getValue(
                new ResourceLocation("baseminerals", ORES[i] + "_ore")), i);
        TreeMap<String, String> coordinates = new TreeMap<>();
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
        for (int cx = 64; cx < 80; cx++) for (int cz = 64; cz < 80; cz++) {
            Chunk chunk = world.getChunk(cx, cz);
            for (int x = 0; x < 16; x++) for (int z = 0; z < 16; z++) for (int y = 0; y < 256; y++) {
                pos.setPos(cx * 16 + x, y, cz * 16 + z);
                Block block = chunk.getBlockState(pos).getBlock();
                Integer index = indices.get(block);
                if (index == null) {
                    if ("baseminerals".equals(block.getRegistryName().getNamespace()))
                        throw new AssertionError("Undeclared generated mineral block " + block.getRegistryName());
                    continue;
                }
                if (!overworld || y < HEIGHTS[index][0] || y > HEIGHTS[index][1])
                    throw new AssertionError("Unexpected ore " + ORES[index] + " at " + pos + " in " + world.getDimension().getType());
                histogram[index][y]++;
                coordinates.put(pos.getX() + "," + y + "," + pos.getZ(), ORES[index]);
            }
        }
        JsonObject result = new JsonObject(), counts = new JsonObject(), hist = new JsonObject();
        for (int i = 0; i < ORES.length; i++) {
            int count = java.util.Arrays.stream(histogram[i]).sum();
            String disabled = System.getProperty("mineralprobe.disabled", "");
            boolean expectDisabled = "all".equals(disabled) || java.util.Arrays.asList(disabled.split(",")).contains(ORES[i]);
            if (overworld && (expectDisabled ? count != 0 : count == 0))
                throw new AssertionError("Unexpected count " + count + " for " + ORES[i] + "; disabled=" + expectDisabled);
            counts.addProperty(ORES[i], count);
            JsonObject heights = new JsonObject();
            for (int y = 0; y < 256; y++) if (histogram[i][y] != 0) heights.addProperty(String.valueOf(y), histogram[i][y]);
            hist.add(ORES[i], heights);
        }
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        for (Map.Entry<String, String> entry : coordinates.entrySet())
            digest.update((entry.getKey() + "=" + entry.getValue() + "\n").getBytes(StandardCharsets.UTF_8));
        StringBuilder hex = new StringBuilder();
        for (byte b : digest.digest()) hex.append(String.format("%02x", b & 255));
        result.add("counts", counts); result.add("height_histograms", hist);
        result.addProperty("sorted_coordinate_sha256", hex.toString());
        result.addProperty("unchanged_after_later_chunks", true);
        return result;
    }
}
