package zone.moddev.mc.baseminerals.content;

import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.block.BlockState;
import net.minecraft.fluid.FlowingFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.IFluidState;
import net.minecraft.item.Item;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraft.util.ResourceLocation;

/** Common flow behaviour for the molten metals. */
public abstract class MoltenMineralFluid extends FlowingFluid {
    private final MineralContent.Molten content;

    protected MoltenMineralFluid(MineralContent.Molten content) {
        this.content = content;
    }

    @Override protected FluidAttributes createAttributes() {
        return FluidAttributes.builder(new ResourceLocation("baseminerals", "block/molten_metal_still"),
                new ResourceLocation("baseminerals", "block/molten_metal_flow"))
                .color(0xFFC5C8C1).density(2000).viscosity(10000).temperature(330).luminosity(10).build(this);
    }


    @Override public Fluid getFlowingFluid() { return content.flowing; }
    @Override public Fluid getStillFluid() { return content.source; }
    @Override public Item getFilledBucket() { return content.bucket; }

    @Override protected boolean canSourcesMultiply() { return false; }
    @Override protected void beforeReplacingBlock(IWorld world, BlockPos pos, BlockState state) {
        net.minecraft.block.Block.spawnDrops(state, world.getWorld(), pos);
    }
    @Override public int getSlopeFindDistance(IWorldReader world) { return 2; }
    @Override public int getLevelDecreasePerBlock(IWorldReader world) { return 2; }
    @Override public int getTickRate(IWorldReader world) { return 30; }
    @Override protected float getExplosionResistance() { return 100.0F; }
    @Override public boolean isEquivalentTo(Fluid fluid) {
        return fluid == content.source || fluid == content.flowing;
    }
    @Override protected boolean canDisplace(IFluidState state, net.minecraft.world.IBlockReader world,
            BlockPos pos, Fluid fluid, Direction direction) {
        return direction == Direction.DOWN && !fluid.isEquivalentTo(this);
    }
    @Override public BlockState getBlockState(IFluidState state) {
        return content.block.getDefaultState()
                .with(FlowingFluidBlock.LEVEL, Integer.valueOf(getLevelFromState(state)));
    }

    public static final class Flowing extends MoltenMineralFluid {
        public Flowing(MineralContent.Molten content) { super(content); }
        @Override protected void fillStateContainer(StateContainer.Builder<Fluid, IFluidState> builder) {
            super.fillStateContainer(builder);
            builder.add(LEVEL_1_8);
        }
        @Override public int getLevel(IFluidState state) { return state.get(LEVEL_1_8); }
        @Override public boolean isSource(IFluidState state) { return false; }
    }

    public static final class Source extends MoltenMineralFluid {
        public Source(MineralContent.Molten content) { super(content); }
        @Override public int getLevel(IFluidState state) { return 8; }
        @Override public boolean isSource(IFluidState state) { return true; }
    }
}
