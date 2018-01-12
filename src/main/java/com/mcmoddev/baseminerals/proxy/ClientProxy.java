package com.mcmoddev.baseminerals.proxy;

import com.mcmoddev.baseminerals.init.Blocks;
import com.mcmoddev.baseminerals.init.Fluids;
import com.mcmoddev.baseminerals.init.Items;
import com.mcmoddev.lib.client.registrations.RegistrationHelper;
import com.mcmoddev.lib.material.MMDMaterial;

import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Base Minerals Client Proxy
 *
 * @author Jasmine Iwanek
 *
 */
public class ClientProxy extends CommonProxy {

	@Override
	public void preInit(FMLPreInitializationEvent event) {
		super.preInit(event);
		
		MinecraftForge.EVENT_BUS.register(this);
	}

    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent event) {
		for (final String name : Items.getItemRegistry().keySet()) {
			RegistrationHelper.registerItemRender(name);
		}

		for (final String name : Blocks.getBlockRegistry().keySet()) {
			RegistrationHelper.registerBlockRender(name);
		}
    }

    @SubscribeEvent
	public void fluidRendering(RegistryEvent.Register<MMDMaterial> event) {
		for (final String name : Fluids.getFluidBlockRegistry().keySet()) {
			RegistrationHelper.registerFluidRender(name);
		}
	}
}
