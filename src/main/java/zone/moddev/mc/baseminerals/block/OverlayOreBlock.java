package zone.moddev.mc.baseminerals.block;

import com.mcmoddev.lib.block.BlockMMDOre;
import com.mcmoddev.lib.material.MMDMaterial;
import net.minecraft.util.BlockRenderLayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/** Uses the shared ore behaviour with alpha testing for overlay models. */
public final class OverlayOreBlock extends BlockMMDOre {
    public OverlayOreBlock(MMDMaterial material) {
        super(material);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT_MIPPED;
    }
}
