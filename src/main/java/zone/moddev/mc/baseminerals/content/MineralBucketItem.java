package zone.moddev.mc.baseminerals.content;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.fluids.capability.wrappers.FluidBucketWrapper;
import zone.moddev.mc.baseminerals.BaseMinerals;
import zone.moddev.mc.lib.api.ItemContext;
import zone.moddev.mc.lib.api.ItemImplementation;
import zone.moddev.mc.lib.api.MaterialExtensions;
import zone.moddev.mc.lib.common.types.MMDItem;
import zone.moddev.mc.lib.common.types.MMDMaterial;

/** A bucket of molten lithium or silicon, made by the {@code baseminerals:bucket} item form. */
public final class MineralBucketItem extends BucketItem {
    public MineralBucketItem(Fluid fluid, Properties properties) {
        super(fluid, properties);
    }

    /** Must run before MMDLib decodes the materials that select {@code baseminerals:bucket}. */
    public static void registerImplementation() {
        MaterialExtensions.GLOBAL.registerItemImplementation(
                ResourceLocation.fromNamespaceAndPath(BaseMinerals.MOD_ID, "bucket"), new Implementation());
    }

    /** Lets fluid tanks and pipes fill from and empty into the buckets. */
    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        ModContent.fluids().values().forEach(fluid -> event.registerItem(Capabilities.FluidHandler.ITEM,
                (stack, context) -> new FluidBucketWrapper(stack), fluid.bucket().get()));
    }

    private static final class Implementation implements ItemImplementation<MineralBucketItem> {
        @Override
        public Class<MineralBucketItem> itemClass() {
            return MineralBucketItem.class;
        }

        @Override
        public MineralBucketItem create(MMDMaterial material, MMDItem definition) {
            throw new IllegalStateException("Created by the runtime registrar with an ItemContext: " + definition.name());
        }

        @Override
        public MineralBucketItem create(ItemContext context) {
            String mineral = context.material().name().orElseThrow().getPath();
            FluidContent fluid = ModContent.fluids().get(mineral);
            if (fluid == null) throw new IllegalStateException("No molten fluid for " + mineral);
            return new MineralBucketItem(fluid.source().get(), context.properties());
        }
    }
}
