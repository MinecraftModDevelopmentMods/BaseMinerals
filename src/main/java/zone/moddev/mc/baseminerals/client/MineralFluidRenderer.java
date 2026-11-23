package zone.moddev.mc.baseminerals.client;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import zone.moddev.mc.baseminerals.content.MineralContent;

/** Forge fluid attributes provide the world sprites; item colours tint the bucket contents. */
@OnlyIn(Dist.CLIENT)
public final class MineralFluidRenderer {
    private MineralFluidRenderer() {}
    public static void register() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(MineralFluidRenderer::bucketColours);
    }
    private static void bucketColours(ColorHandlerEvent.Item event) {
        for (String material : new String[] {"lithium", "silicon"}) {
            event.getItemColors().register((stack, tint) -> tint == 1 ? 0xC5C8C1 : 0xFFFFFF,
                    MineralContent.bucket(material));
        }
    }
}
