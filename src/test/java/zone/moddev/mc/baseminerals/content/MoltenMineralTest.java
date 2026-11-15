package zone.moddev.mc.baseminerals.content;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem;
import net.neoforged.testframework.junit.EphemeralTestServerProvider;
import zone.moddev.mc.baseminerals.LegacyIds;

/**
 * Molten lithium and silicon flow and fill buckets as they did on 1.13.2. Burning needs a
 * world, so {@code MoltenMineralGameTests} covers it.
 */
@ExtendWith(EphemeralTestServerProvider.class)
class MoltenMineralTest {
    @Test
    void fluidsFlowSlowlyAndNeverMakeNewSources(MinecraftServer server) {
        ServerLevel level = server.overworld();
        for (String name : LegacyIds.MOLTEN) {
            FlowingFluid source = assertInstanceOf(FlowingFluid.class, fluid(name), name);
            FlowingFluid flowing = assertInstanceOf(FlowingFluid.class, fluid("flowing_" + name), name);
            assertSame(source, flowing.getSource(), name);
            assertSame(flowing, source.getFlowing(), name);
            assertTrue(source.isSource(source.defaultFluidState()), name);
            assertEquals(30, source.getTickDelay(level), name);
            assertEquals(100.0F, source.defaultFluidState().getExplosionResistance(), name);
            assertFalse(source.getFluidType().canConvertToSource(source.defaultFluidState(), level, BlockPos.ZERO),
                    name);
        }
    }

    @Test
    void fluidTypesTintTheMoltenMetalTexture() {
        for (String name : LegacyIds.MOLTEN) {
            FluidType type = fluid(name).getFluidType();
            MineralFluidType mineral = assertInstanceOf(MineralFluidType.class, type, name);
            // The colour 1.13.2 drew both molten minerals in, now from their MMDLib materials.
            assertEquals(0xFFC5C8C1, mineral.colour(), name);
            assertEquals("fluid_type.baseminerals." + name, type.getDescriptionId(), name);
            assertEquals(MineralFluidType.STILL, mineral.clientExtensions().getStillTexture(), name);
            assertEquals(MineralFluidType.FLOWING, mineral.clientExtensions().getFlowingTexture(), name);
        }
    }

    @Test
    void moltenBlocksAreLiquidsWithAKeptBlockItem() {
        for (String name : LegacyIds.MOLTEN) {
            var block = BuiltInRegistries.BLOCK.get(LegacyIds.id(name));
            LiquidBlock liquid = assertInstanceOf(MoltenMineralBlock.class, block, name);
            assertSame(fluid(name), liquid.fluid, name);
            BlockState state = liquid.defaultBlockState();
            assertTrue(state.canBeReplaced(), name);
            assertTrue(state.liquid(), name);
            // Kept so stacks of the 1.13.2 block item in old inventories survive.
            assertSame(block, ((net.minecraft.world.item.BlockItem) BuiltInRegistries.ITEM.get(LegacyIds.id(name)))
                    .getBlock(), name);
        }
    }

    @Test
    void bucketsExposeTheirFluidToTanks() {
        for (String name : LegacyIds.MOLTEN) {
            ItemStack bucket = new ItemStack(BuiltInRegistries.ITEM.get(LegacyIds.id(name + "_bucket")));
            IFluidHandlerItem handler = bucket.getCapability(Capabilities.FluidHandler.ITEM);
            assertNotNull(handler, name);
            FluidStack drained = handler.drain(FluidType.BUCKET_VOLUME, IFluidHandler.FluidAction.SIMULATE);
            assertSame(fluid(name), drained.getFluid(), name);
            assertEquals(FluidType.BUCKET_VOLUME, drained.getAmount(), name);
        }
    }

    private static Fluid fluid(String path) {
        return BuiltInRegistries.FLUID.get(LegacyIds.id(path));
    }
}
