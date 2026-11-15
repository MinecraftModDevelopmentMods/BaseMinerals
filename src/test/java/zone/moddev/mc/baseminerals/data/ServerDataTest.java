package zone.moddev.mc.baseminerals.data;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;
import net.neoforged.testframework.junit.EphemeralTestServerProvider;
import zone.moddev.mc.baseminerals.BaseMinerals;
import zone.moddev.mc.baseminerals.LegacyIds;

/** Recipes, tags and loot tables load in a real server and do what the 1.13.2 data did. */
@ExtendWith(EphemeralTestServerProvider.class)
class ServerDataTest {
    /** The number of recipe files under {@code data/baseminerals/recipe}. */
    private static final int RECIPE_FILES = 52;

    @Test
    void everyRecipeLoads(MinecraftServer server) {
        long ours = server.getRecipeManager().getRecipes().stream()
                .filter(holder -> BaseMinerals.MOD_ID.equals(holder.id().getNamespace())).count();
        assertEquals(RECIPE_FILES, ours, "A Base Minerals recipe failed to load; check the server log");
    }

    @Test
    void crackhammersCrushEachOreIntoTwoPowders(MinecraftServer server) {
        Map<ResourceLocation, RecipeHolder<?>> recipes = server.getRecipeManager().getRecipes().stream()
                .collect(Collectors.toMap(RecipeHolder::id, Function.identity()));
        for (String name : LegacyIds.ORES) {
            RecipeHolder<?> holder = recipes.get(LegacyIds.id(name + "_ore_crushing"));
            assertNotNull(holder, name);
            Recipe<?> recipe = holder.value();
            assertEquals(ResourceLocation.fromNamespaceAndPath("mmdlib", "crushing"),
                    BuiltInRegistries.RECIPE_TYPE.getKey(recipe.getType()), name);
            ItemStack result = recipe.getResultItem(server.registryAccess());
            assertEquals(item(name + "_powder"), result.getItem(), name);
            assertEquals(2, result.getCount(), name);
            assertTrue(recipe.getIngredients().get(0).test(new ItemStack(item(name + "_ore"))), name);
        }
    }

    @Test
    void siliconPowderSmeltsIntoAnIngot(MinecraftServer server) {
        var input = new SingleRecipeInput(new ItemStack(item("silicon_powder")));
        var recipe = server.getRecipeManager().getRecipeFor(RecipeType.SMELTING, input, server.overworld());
        assertTrue(recipe.isPresent());
        assertEquals(item("silicon_ingot"), recipe.get().value().getResultItem(server.registryAccess()).getItem());
    }

    @Test
    void gunpowderAcceptsVanillaFuelAndMineralDusts(MinecraftServer server) {
        var gunpowder = server.getRecipeManager().getRecipes().stream()
                .filter(holder -> holder.id().equals(LegacyIds.id("gunpowder_coal_saltpeter")))
                .findFirst().orElseThrow().value();
        assertEquals(Items.GUNPOWDER, gunpowder.getResultItem(server.registryAccess()).getItem());
        var ingredients = gunpowder.getIngredients();
        assertTrue(ingredients.stream().anyMatch(i -> i.test(new ItemStack(Items.COAL))));
        assertTrue(ingredients.stream().anyMatch(i -> i.test(new ItemStack(item("sulfur_powder")))));
        assertTrue(ingredients.stream().anyMatch(i -> i.test(new ItemStack(item("saltpeter_powder")))));
    }

    @Test
    void commonTagsNameTheMineralItemsAndBlocks() {
        for (String name : LegacyIds.MINERALS) {
            assertItemTag("c:dusts/" + name, name + "_powder");
            assertItemTag("c:dusts", name + "_powder");
            assertItemTag("c:tiny_dusts/" + name, name + "_smallpowder");
            assertItemTag("c:storage_blocks/" + name, name + "_block");
            assertBlockTag("c:storage_blocks/" + name, name + "_block");
        }
        for (String name : LegacyIds.ORES) {
            assertItemTag("c:ores/" + name, name + "_ore");
            assertItemTag("c:ores", name + "_ore");
            assertBlockTag("c:ores/" + name, name + "_ore");
            assertBlockTag("c:ores", name + "_ore");
        }
        for (String name : LegacyIds.MOLTEN) {
            assertItemTag("c:ingots/" + name, name + "_ingot");
            assertItemTag("c:nuggets/" + name, name + "_nugget");
        }
    }

    @Test
    void mineralBlocksNeedAStonePickaxe() {
        for (ResourceLocation id : LegacyIds.solidBlocks()) {
            var block = BuiltInRegistries.BLOCK.getHolderOrThrow(ResourceKey.create(Registries.BLOCK, id));
            assertTrue(block.is(BlockTags.MINEABLE_WITH_PICKAXE), id::toString);
            assertTrue(block.is(BlockTags.NEEDS_STONE_TOOL), id::toString);
        }
    }

    @Test
    void mineralBlocksDropThemselves(MinecraftServer server) {
        for (ResourceLocation id : LegacyIds.solidBlocks()) {
            ResourceKey<LootTable> key = BuiltInRegistries.BLOCK.get(id).getLootTable();
            assertEquals(id.withPrefix("blocks/"), key.location(), id::toString);
            LootTable table = server.reloadableRegistries().getLootTable(key);
            assertNotSame(LootTable.EMPTY, table, id::toString);
        }
        // Molten blocks have no loot, as liquids don't.
        for (String name : LegacyIds.MOLTEN) {
            Block block = BuiltInRegistries.BLOCK.get(LegacyIds.id(name));
            assertEquals(net.minecraft.world.level.storage.loot.BuiltInLootTables.EMPTY, block.getLootTable(), name);
        }
    }

    private static Item item(String path) {
        return BuiltInRegistries.ITEM.get(LegacyIds.id(path));
    }

    private static void assertItemTag(String tag, String path) {
        TagKey<Item> key = TagKey.create(Registries.ITEM, ResourceLocation.parse(tag));
        assertTrue(new ItemStack(item(path)).is(key), path + " in #" + tag);
    }

    private static void assertBlockTag(String tag, String path) {
        TagKey<Block> key = TagKey.create(Registries.BLOCK, ResourceLocation.parse(tag));
        assertTrue(BuiltInRegistries.BLOCK.get(LegacyIds.id(path)).defaultBlockState().is(key),
                path + " in #" + tag);
    }
}
