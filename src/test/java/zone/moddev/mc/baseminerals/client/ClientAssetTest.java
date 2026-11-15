package zone.moddev.mc.baseminerals.client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.fml.ModList;
import zone.moddev.mc.baseminerals.BaseMinerals;
import zone.moddev.mc.baseminerals.LegacyIds;

/**
 * Every block and item has a blockstate, model, textures and English name. The
 * client loads these lazily, so a missing file would otherwise only show as a
 * purple-and-black square in game.
 */
class ClientAssetTest {
    @Test
    void everyBlockHasABlockstateWhoseModelsExist() throws IOException {
        for (ResourceLocation id : LegacyIds.blocks()) {
            JsonObject state = json("assets/baseminerals/blockstates/" + id.getPath() + ".json");
            for (var variant : state.getAsJsonObject("variants").entrySet()) {
                String model = variant.getValue().getAsJsonObject().get("model").getAsString();
                assertModelResolves(ResourceLocation.parse(model), "blockstate " + id);
            }
        }
    }

    @Test
    void everyItemHasAModelWhoseTexturesExist() throws IOException {
        for (ResourceLocation id : LegacyIds.allItems()) {
            assertModelResolves(id.withPrefix("item/"), "item " + id);
        }
    }

    @Test
    void everyBlockItemAndFluidIsNamedInEnglish() throws IOException {
        JsonObject english = json("assets/baseminerals/lang/en_us.json");
        List<String> missing = new ArrayList<>();
        for (ResourceLocation id : LegacyIds.allItems()) {
            String key = BuiltInRegistries.ITEM.get(id).getDescriptionId();
            if (!english.has(key)) missing.add(key);
        }
        for (String name : LegacyIds.MOLTEN) {
            String key = BuiltInRegistries.FLUID.get(LegacyIds.id(name)).getFluidType().getDescriptionId();
            if (!english.has(key)) missing.add(key);
        }
        if (!english.has("itemGroup.baseminerals.materials")) missing.add("itemGroup.baseminerals.materials");
        assertEquals(List.of(), missing);
    }

    @Test
    void translationsUseOnlyKnownKeys() throws IOException {
        Set<String> english = json("assets/baseminerals/lang/en_us.json").keySet();
        for (String language : List.of("de_de", "ru_ru")) {
            Set<String> keys = json("assets/baseminerals/lang/" + language + ".json").keySet();
            assertTrue(english.containsAll(keys), language + " has keys English lacks: " + keys);
        }
    }

    @Test
    void oresDrawTheirOverlayOnStone() throws IOException {
        JsonObject overlay = json("assets/baseminerals/models/block/ore_overlay.json");
        assertEquals("minecraft:cutout_mipped", overlay.get("render_type").getAsString());
        for (String name : LegacyIds.ORES) {
            JsonObject model = json("assets/baseminerals/models/block/" + name + "_ore.json");
            assertEquals("baseminerals:block/ore_overlay", model.get("parent").getAsString(), name);
            assertEquals("minecraft:block/stone", model.getAsJsonObject("textures").get("base").getAsString(), name);
        }
    }

    /** Checks that the model exists, and so do its parents and textures in this mod. */
    private static void assertModelResolves(ResourceLocation model, String owner) throws IOException {
        if (!BaseMinerals.MOD_ID.equals(model.getNamespace())) return;
        Path file = resource("assets/baseminerals/models/" + model.getPath() + ".json");
        assertTrue(Files.isRegularFile(file), owner + " needs model " + model);
        JsonObject json = read(file);
        if (json.has("parent")) assertModelResolves(ResourceLocation.parse(json.get("parent").getAsString()), owner);
        if (json.has("textures")) {
            for (var texture : json.getAsJsonObject("textures").entrySet()) {
                String value = texture.getValue().getAsString();
                if (value.startsWith("#")) continue;
                ResourceLocation id = ResourceLocation.parse(value);
                if (!BaseMinerals.MOD_ID.equals(id.getNamespace())) continue;
                Path png = resource("assets/baseminerals/textures/" + id.getPath() + ".png");
                assertTrue(Files.isRegularFile(png), owner + " needs texture " + id);
            }
        }
        if (json.has("fluid")) {
            ResourceLocation fluid = ResourceLocation.parse(json.get("fluid").getAsString());
            assertTrue(BuiltInRegistries.FLUID.containsKey(fluid), owner + " draws unknown fluid " + fluid);
        }
    }

    private static Path resource(String path) {
        return ModList.get().getModFileById(BaseMinerals.MOD_ID).getFile().findResource(path.split("/"));
    }

    private static JsonObject json(String path) throws IOException {
        Path file = resource(path);
        assertTrue(Files.isRegularFile(file), "missing " + path);
        return read(file);
    }

    private static JsonObject read(Path file) throws IOException {
        try (Reader reader = Files.newBufferedReader(file)) {
            JsonElement element = JsonParser.parseReader(reader);
            return element.getAsJsonObject();
        }
    }
}
