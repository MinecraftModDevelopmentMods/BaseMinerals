package zone.moddev.mc.baseminerals.content;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.EmptyBlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import zone.moddev.mc.baseminerals.BaseMinerals;
import zone.moddev.mc.baseminerals.LegacyIds;
import zone.moddev.mc.lib.common.runtime.MaterialBacked;

/** Every 1.13.2 ID is registered, nothing else is, and blocks keep their 1.13.2 behaviour. */
class RegistryContentTest {
    @Test
    void registersExactlyTheLegacyBlocksItemsAndFluids() {
        assertEquals(Set.copyOf(LegacyIds.blocks()), ours(BuiltInRegistries.BLOCK.keySet()));
        assertEquals(Set.copyOf(LegacyIds.items()), ours(BuiltInRegistries.ITEM.keySet()));
        assertEquals(Set.copyOf(LegacyIds.fluids()), ours(BuiltInRegistries.FLUID.keySet()));
    }

    /** Explosion resistance and sound need a world; the game tests check those. */
    @Test
    void mineralBlocksAndOresKeepTheirStoneProperties() {
        for (ResourceLocation id : LegacyIds.solidBlocks()) {
            Block block = BuiltInRegistries.BLOCK.get(id);
            BlockState state = block.defaultBlockState();
            assertEquals(3.0F, state.getDestroySpeed(EmptyBlockGetter.INSTANCE, BlockPos.ZERO), id::toString);
            assertTrue(state.requiresCorrectToolForDrops(), id::toString);
            assertSame(MapColor.STONE, state.getMapColor(EmptyBlockGetter.INSTANCE, BlockPos.ZERO), id::toString);
            assertInstanceOf(BlockItem.class, BuiltInRegistries.ITEM.get(id), id::toString);
        }
    }

    @Test
    void oresUseMmdLibsOreForm() {
        for (String name : LegacyIds.ORES) {
            Block ore = BuiltInRegistries.BLOCK.get(LegacyIds.id(name + "_ore"));
            // MMDLib's ore form; the game test checks it drops no XP, like the 1.13.2 plain blocks.
            assertInstanceOf(DropExperienceBlock.class, ore, name);
        }
    }

    @Test
    void mineralItemsBelongToTheirMaterial() {
        List<String> forms = List.of("powder", "smallpowder");
        for (String name : LegacyIds.MINERALS) {
            for (String form : forms) {
                var item = BuiltInRegistries.ITEM.get(LegacyIds.id(name + "_" + form));
                MaterialBacked backed = assertInstanceOf(MaterialBacked.class, item, name + "_" + form);
                assertEquals(LegacyIds.id(name), backed.material().name().orElseThrow());
            }
        }
    }

    @Test
    void bucketsHoldOneAndReturnAnEmptyBucket() {
        for (String name : LegacyIds.MOLTEN) {
            var bucket = BuiltInRegistries.ITEM.get(LegacyIds.id(name + "_bucket"));
            MineralBucketItem item = assertInstanceOf(MineralBucketItem.class, bucket, name);
            assertEquals(1, item.getDefaultMaxStackSize(), name);
            assertSame(Items.BUCKET, new ItemStack(item).getCraftingRemainingItem().getItem(), name);
            assertSame(BuiltInRegistries.FLUID.get(LegacyIds.id(name)), item.content, name);
        }
    }

    private static Set<ResourceLocation> ours(Set<ResourceLocation> ids) {
        return ids.stream().filter(id -> BaseMinerals.MOD_ID.equals(id.getNamespace())).collect(Collectors.toSet());
    }
}
