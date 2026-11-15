package zone.moddev.mc.baseminerals;

import net.minecraft.item.crafting.RecipeSerializers;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DeferredWorkQueue;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import zone.moddev.mc.baseminerals.client.MineralFluidRenderer;
import zone.moddev.mc.baseminerals.recipe.SharedCrushingSerializer;
import zone.moddev.mc.baseminerals.worldgen.BaseMineralsWorldgenProvider;

@Mod(BaseMinerals.MOD_ID)
public final class BaseMinerals {
    public static final String MOD_ID = "baseminerals";

    public BaseMinerals() {
        DistExecutor.runWhenOn(Dist.CLIENT, () -> MineralFluidRenderer::register);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueProvider);
    }

    private void setup(FMLCommonSetupEvent event) {
        DeferredWorkQueue.runLater(() -> RecipeSerializers.register(new SharedCrushingSerializer()));
    }

    private void enqueueProvider(InterModEnqueueEvent event) {
        BaseMineralsWorldgenProvider.enqueue();
    }
}
