package zone.moddev.mc.baseminerals.worldgen;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.minecraft.core.registries.BuiltInRegistries;
import zone.moddev.mc.baseminerals.BaseMinerals;
import zone.moddev.mc.baseminerals.LegacyIds;
import zone.moddev.mc.orespawn.api.OreSpawnApi;
import zone.moddev.mc.orespawn.api.ProviderStatus;
import zone.moddev.mc.orespawn.api.WorldgenProvider;

/** The OreSpawn declarations keep the 1.13.2 rule IDs and placement. */
class WorldgenProviderTest {
    @Test
    void declaresEveryOreUnderItsSavedRuleId() {
        JsonObject ores = provider().getAsJsonObject("ores");
        Set<String> expected = new HashSet<>();
        for (String name : LegacyIds.ORES) expected.add("baseminerals:legacy/" + name + "_ore");
        assertEquals(expected, ores.keySet());
        for (String name : LegacyIds.ORES) {
            JsonObject ore = ores.getAsJsonObject("baseminerals:legacy/" + name + "_ore");
            assertEquals("baseminerals:" + name + "_ore", ore.get("block").getAsString());
            assertEquals("orespawn:" + name, ore.get("material").getAsString());
            assertFalse(ore.get("native_generation").getAsBoolean(), name);
            assertFalse(ore.get("retrogen").getAsBoolean(), name);
            assertTrue(ore.getAsJsonObject("dimensions").has("minecraft:overworld"), name);
            assertEquals(1, ore.getAsJsonObject("dimension_selectors").size(), name);
        }
    }

    @Test
    void keepsTheFreshWorldPlacementTable() {
        JsonObject ores = provider().getAsJsonObject("ores");
        // Mineral, min Y, max Y, attempts per chunk, min and max quantity (docs/ORE_GENERATION.md).
        Object[][] table = {
            {"lithium", -32, 56, 3.0, 15, 15},
            {"niter", 32, 88, 1.5, 12, 12},
            {"salt", 16, 80, .55, 20, 20},
            {"sulfur", -32, 64, 1.5, 15, 15},
            {"phosphorus", -16, 64, 1.5, 15, 15},
            {"potash", 8, 64, .8, 18, 18},
            {"saltpeter", 40, 96, 1.25, 12, 12},
        };
        for (Object[] row : table) {
            JsonObject placement = ores.getAsJsonObject("baseminerals:legacy/" + row[0] + "_ore")
                    .getAsJsonObject("dimensions").getAsJsonObject("minecraft:overworld");
            assertEquals(row[1], placement.get("min_y").getAsInt(), row[0] + " min_y");
            assertEquals(row[2], placement.get("max_y").getAsInt(), row[0] + " max_y");
            assertEquals((double) row[3], placement.get("frequency").getAsDouble(), 1e-9, row[0] + " frequency");
            assertEquals(row[4], placement.get("quantity").getAsInt(), row[0] + " fixed quantity");
            assertEquals(row[4], row[5]);
            assertEquals("orespawn:standard", placement.get("placement_channel").getAsString());
            Set<String> hosts = new HashSet<>();
            for (JsonElement host : placement.getAsJsonArray("host_blocks")) {
                hosts.add(host.isJsonObject() ? host.getAsJsonObject().get("block").getAsString() : host.getAsString());
            }
            assertEquals(Set.of("minecraft:stone", "minecraft:deepslate"), hosts);
            assertEquals(8, placement.getAsJsonObject("geomes").size());
        }
    }

    @Test
    void customDimensionFallbackKeepsItsPreviousBudgetAndRouting() {
        JsonObject ores = provider().getAsJsonObject("ores");
        for (BaseMineralsWorldgenProvider.Ore rule : BaseMineralsWorldgenProvider.ORES) {
            JsonObject ore = ores.getAsJsonObject("baseminerals:legacy/" + rule.name() + "_ore");
            JsonObject fallback = ore.getAsJsonObject("dimension_selectors").getAsJsonObject("orespawn:all_except_nether_end");
            assertEquals(rule.frequency(), fallback.get("frequency").getAsDouble(), 1e-9);
            assertEquals(rule.minY(), fallback.get("min_y").getAsInt());
            assertEquals(rule.maxY(), fallback.get("max_y").getAsInt());
            assertEquals(rule.minQuantity(), fallback.get("min_quantity").getAsInt());
            assertEquals(rule.maxQuantity(), fallback.get("max_quantity").getAsInt());
            assertFalse(ore.getAsJsonObject("dimensions").has("minecraft:the_nether"));
            assertFalse(ore.getAsJsonObject("dimensions").has("minecraft:the_end"));
        }
    }

    @Test
    void lithiumKeepsItsVeinLength() {
        JsonObject placement = provider().getAsJsonObject("ores").getAsJsonObject("baseminerals:legacy/lithium_ore")
                .getAsJsonObject("dimensions").getAsJsonObject("minecraft:overworld");
        JsonObject pattern = placement.getAsJsonObject("pattern");
        assertEquals("orespawn:vein", pattern.get("type").getAsString());
        assertEquals(8, pattern.getAsJsonObject("settings").get("length").getAsInt());
        assertEquals(3, pattern.getAsJsonObject("settings").get("node_size").getAsInt());
    }

    @Test
    void skipsOresWhoseBlocksAreMissing() {
        assertTrue(BaseMineralsWorldgenProvider.build(id -> false).isEmpty());
        JsonObject ores = BaseMineralsWorldgenProvider.build(id -> id.getPath().equals("salt_ore"))
                .map(WorldgenProvider::toJson).orElseThrow().getAsJsonObject("ores");
        assertEquals(Set.of("baseminerals:legacy/salt_ore"), ores.keySet());
    }

    @Test
    void oreSpawnReceivedTheProvider() {
        // The mod enqueues during InterModEnqueueEvent, which the test launch runs.
        assertNotEquals(ProviderStatus.INACTIVE, OreSpawnApi.getProviderStatus(BaseMinerals.MOD_ID));
    }

    private static JsonObject provider() {
        return BaseMineralsWorldgenProvider.build(BuiltInRegistries.BLOCK::containsKey)
                .orElseThrow().toJson();
    }
}
