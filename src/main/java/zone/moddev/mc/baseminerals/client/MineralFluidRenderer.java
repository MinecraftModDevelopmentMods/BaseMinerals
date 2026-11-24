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
        FMLJavaModLoadingContext.get().getModEventBus().addListener(MineralFluidRenderer::rendering);
    }
    private static void rendering(net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent event) {
        net.minecraftforge.fml.DeferredWorkQueue.runLater(() -> {
            for (net.minecraft.block.Block block : net.minecraftforge.registries.ForgeRegistries.BLOCKS) {
                if (!"baseminerals".equals(block.getRegistryName().getNamespace())) continue;
                if (block instanceof zone.moddev.mc.baseminerals.content.MoltenMineralBlock)
                    net.minecraft.client.renderer.RenderTypeLookup.setRenderLayer(block, net.minecraft.client.renderer.RenderType.getTranslucent());
                else if (block.getRegistryName().getPath().endsWith("_ore"))
                    net.minecraft.client.renderer.RenderTypeLookup.setRenderLayer(block, net.minecraft.client.renderer.RenderType.getCutoutMipped());
            }
            for (net.minecraft.fluid.Fluid fluid : net.minecraftforge.registries.ForgeRegistries.FLUIDS)
                if (MineralContent.ownsFluid(fluid))
                    net.minecraft.client.renderer.RenderTypeLookup.setRenderLayer(fluid, net.minecraft.client.renderer.RenderType.getTranslucent());
        });
    }
    private static void bucketColours(ColorHandlerEvent.Item event) {
        for (String material : new String[] {"lithium", "silicon"}) {
            event.getItemColors().register((stack, tint) -> tint == 1 ? 0xC5C8C1 : 0xFFFFFF,
                    MineralContent.bucket(material));
        }
    }
}
