package zone.moddev.mc.baseminerals.content;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;

/** Molten lithium and silicon burn like lava, as they did when their blocks used lava's material. */
public final class MoltenMineralBlock extends LiquidBlock {
    public MoltenMineralBlock(FlowingFluid fluid, Properties properties) {
        super(fluid, properties);
    }

    @Override
    protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        super.entityInside(state, level, pos, entity);
        // Respects fire immunity and Fire Resistance, as lava does.
        if (!level.isClientSide()) entity.lavaHurt();
    }
}
