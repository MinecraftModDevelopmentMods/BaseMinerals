package com.mcmoddev.baseminerals.util;

import java.io.File;

import com.mcmoddev.baseminerals.BaseMinerals;
import com.mcmoddev.baseminerals.data.MaterialNames;
import com.mcmoddev.lib.registry.CrusherRecipeRegistry;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * @author Jasmine Iwanek
 *
 */
public class Config extends com.mcmoddev.lib.util.ConfigBase {

	private static Configuration configuration;
	private static final String CONFIG_FILE = "config/BaseMinerals.cfg";
	private static final String MATERIALS_CAT = "Minerals";

	@SubscribeEvent
	public void onConfigChange(ConfigChangedEvent.OnConfigChangedEvent e) {
		if (e.getModID().equals(BaseMinerals.MODID)) {
			init();
		}
	}

	public static void init() {
		if (configuration == null) {
			configuration = new Configuration(new File(CONFIG_FILE));
			MinecraftForge.EVENT_BUS.register(new Config());
		}

		// MATERIALS
		Options.materialEnabled(MaterialNames.CHARCOAL, configuration.getBoolean("EnableCharcoal", MATERIALS_CAT, true,
				"Enable Charcoal Items and Materials"));
		Options.materialEnabled(MaterialNames.LITHIUM, configuration.getBoolean("EnableLithium", MATERIALS_CAT, true,
				"Enable Lithium Items and Materials"));
		Options.materialEnabled(MaterialNames.NITER, configuration.getBoolean("EnableNiter", MATERIALS_CAT, true,
				"Enable Niter Items and Materials"));
		Options.materialEnabled(MaterialNames.PHOSPHORUS, configuration.getBoolean("EnablePhosphorus", MATERIALS_CAT, true,
				"Enable Phosphorus Items and Materials"));
		Options.materialEnabled(MaterialNames.POTASH, configuration.getBoolean("EnablePotash", MATERIALS_CAT, true,
				"Enable Potash Items and Materials"));
		Options.materialEnabled(MaterialNames.SALT, configuration.getBoolean("EnableSalt", MATERIALS_CAT, true,
				"Enable Salt Items and Materials"));
		Options.materialEnabled(MaterialNames.SALTPETER, configuration.getBoolean("EnableSaltpeter", MATERIALS_CAT, true,
				"Enable Saltpeter Items and Materials"));
		Options.materialEnabled(MaterialNames.SILICON, configuration.getBoolean("EnableSilicon", MATERIALS_CAT, true,
				"Enable Silicon Items and Materials"));
		Options.materialEnabled(MaterialNames.SULFUR, configuration.getBoolean("EnableSulfur", MATERIALS_CAT, true,
				"Enable Sulfur Items and Materials"));

		if (configuration.hasChanged()) {
			configuration.save();
		}
	}

	public static void postInit() {
		CrusherRecipeRegistry.getInstance().clearCache();
	}
}
