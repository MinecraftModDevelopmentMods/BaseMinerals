package zone.moddev.mc.baseminerals.proxy;

import com.mcmoddev.lib.client.registrations.RegistrationHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import zone.moddev.mc.baseminerals.BaseMinerals;
import zone.moddev.mc.baseminerals.client.BaseMineralsConfigScreen;
import zone.moddev.mc.baseminerals.init.Blocks;
import zone.moddev.mc.baseminerals.init.Fluids;
import zone.moddev.mc.baseminerals.init.Items;
import zone.moddev.mc.orespawn.api.client.WorldSettingsExtensionRegistry;

public class ClientProxy extends CommonProxy {
    @Override
    public void preInit(FMLPreInitializationEvent event) {
        super.preInit(event);
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public void init(FMLInitializationEvent event) {
        super.init(event);
        WorldSettingsExtensionRegistry.registerConfigScreen(BaseMinerals.MODID, BaseMineralsConfigScreen::new);
    }

    @SubscribeEvent
    public void modelRegistry(ModelRegistryEvent event) {
        Items.getItemRegistry().forEach((name, item) -> {
            if (owned(item.getRegistryName())) RegistrationHelper.registerItemRender(name);
        });
        Blocks.getBlockRegistry().forEach((name, block) -> {
            if (owned(block.getRegistryName())) RegistrationHelper.registerBlockRender(name);
        });
        Fluids.getFluidBlockRegistry().forEach((name, block) -> {
            if (owned(block.getRegistryName())) RegistrationHelper.registerFluidRender(name);
        });
    }

    private static boolean owned(ResourceLocation name) {
        return name != null && BaseMinerals.MODID.equals(name.getNamespace());
    }
}
