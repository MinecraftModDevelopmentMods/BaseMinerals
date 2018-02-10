package com.mcmoddev.baseminerals.proxy;

import com.mcmoddev.baseminerals.BaseMinerals;
import com.mcmoddev.baseminerals.init.Blocks;
import com.mcmoddev.baseminerals.init.Fluids;
import com.mcmoddev.baseminerals.init.Items;
import com.mcmoddev.lib.client.registrations.RegistrationHelper;
import com.mcmoddev.lib.data.Names;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

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

		for (final String name : Items.getItemRegistry().keySet()) {
			if (!name.endsWith(Names.ANVIL.toString()))
				RegistrationHelper.registerItemRender(name);
			else {
				final String[] names = new String[] { "intact", "slightly_damaged", "very_damaged" };
				final Item item = Items.getItemByName(name);
				for (int i = 0; i < 3; i++) {
					final ResourceLocation rl = new ResourceLocation(BaseMinerals.MODID, String.format("%s_%s", name, names[i]));
					ModelLoader.setCustomModelResourceLocation(item, i, new ModelResourceLocation(rl, "inventory"));
				}
			}
		}

		for (final String name : Blocks.getBlockRegistry().keySet()) {
			RegistrationHelper.registerBlockRender(name);
		}

		for (final String name : Fluids.getFluidBlockRegistry().keySet()) {
			RegistrationHelper.registerFluidRender(name);
		}
	}
}
