package zone.moddev.mc.baseminerals.content;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.fluids.FluidType;
import zone.moddev.mc.baseminerals.BaseMinerals;

/** A molten mineral's fluid type, which also describes how the client draws it. */
public final class MineralFluidType extends FluidType {
    static final ResourceLocation STILL =
            ResourceLocation.fromNamespaceAndPath(BaseMinerals.MOD_ID, "block/molten_metal_still");
    static final ResourceLocation FLOWING =
            ResourceLocation.fromNamespaceAndPath(BaseMinerals.MOD_ID, "block/molten_metal_flow");

    private final int colour;

    public MineralFluidType(Properties properties, int colour) {
        super(properties);
        this.colour = 0xFF000000 | colour;
    }

    /** The opaque ARGB tint drawn over the molten metal texture. */
    public int colour() {
        return colour;
    }

    /** Registered through {@code RegisterClientExtensionsEvent} by the client setup. */
    public IClientFluidTypeExtensions clientExtensions() {
        return new IClientFluidTypeExtensions() {
            @Override public ResourceLocation getStillTexture() { return STILL; }
            @Override public ResourceLocation getFlowingTexture() { return FLOWING; }
            @Override public int getTintColor() { return colour; }
        };
    }
}
