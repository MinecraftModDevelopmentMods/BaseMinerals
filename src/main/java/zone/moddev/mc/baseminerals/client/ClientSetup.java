package zone.moddev.mc.baseminerals.client;

import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.item.Item;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.client.model.DynamicFluidContainerModel;
import zone.moddev.mc.baseminerals.BaseMinerals;
import zone.moddev.mc.baseminerals.content.FluidContent;
import zone.moddev.mc.baseminerals.content.ModContent;

/** Client entry point: how molten lithium and silicon and their buckets are drawn. */
@Mod(value = BaseMinerals.MOD_ID, dist = Dist.CLIENT)
public final class ClientSetup {
    public ClientSetup(IEventBus modBus) {
        modBus.addListener(ClientSetup::setup);
        modBus.addListener(ClientSetup::registerClientExtensions);
        modBus.addListener(ClientSetup::registerItemColours);
    }

    private static void setup(FMLClientSetupEvent event) {
        // Molten minerals were drawn on the translucent layer in 1.13.2.
        event.enqueueWork(() -> ModContent.fluids().values().forEach(fluid -> {
            ItemBlockRenderTypes.setRenderLayer(fluid.source().get(), RenderType.translucent());
            ItemBlockRenderTypes.setRenderLayer(fluid.flowing().get(), RenderType.translucent());
        }));
    }

    private static void registerClientExtensions(RegisterClientExtensionsEvent event) {
        for (FluidContent fluid : ModContent.fluids().values()) {
            event.registerFluidType(fluid.type().get().clientExtensions(), fluid.type().get());
        }
    }

    /** Buckets show their molten mineral's colour. */
    private static void registerItemColours(RegisterColorHandlersEvent.Item event) {
        event.register(new DynamicFluidContainerModel.Colors(), ModContent.fluids().values().stream()
                .map(fluid -> (Item) fluid.bucket().get()).toArray(Item[]::new));
    }
}
