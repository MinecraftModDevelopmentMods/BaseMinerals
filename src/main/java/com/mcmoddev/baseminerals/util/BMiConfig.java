package com.mcmoddev.baseminerals.util;

import java.io.File;

import com.mcmoddev.baseminerals.BaseMinerals;
import com.mcmoddev.baseminerals.data.MaterialNames;
import com.mcmoddev.lib.registry.CrusherRecipeRegistry;
import com.mcmoddev.lib.util.Config;
import com.mcmoddev.lib.util.MaterialConfigOptions;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * @author Jasmine Iwanek
 *
 */
public class BMiConfig extends Config {

	private static Configuration configuration;
	private static final String CONFIG_FILE = "config/BaseMinerals.cfg";

	private static final MaterialConfigOptions[] MATERIAL_CONFIG_OPTIONS = new MaterialConfigOptions[]{
			new MaterialConfigOptions(MaterialNames.CHARCOAL, true, true, true, true),
			new MaterialConfigOptions(MaterialNames.LITHIUM, false, true, true, true),
			new MaterialConfigOptions(MaterialNames.NITER, false, true, false, false),
			new MaterialConfigOptions(MaterialNames.PHOSPHORUS, false, true, false, false),
			new MaterialConfigOptions(MaterialNames.POTASH, false, true, false, false),
			new MaterialConfigOptions(MaterialNames.SALT, false, true, false, false),
			new MaterialConfigOptions(MaterialNames.SALTPETER, false, true, false, false),
			new MaterialConfigOptions(MaterialNames.SILICON, false, true, false, false),
			new MaterialConfigOptions(MaterialNames.SULFUR, false, true, false, false)
	};
	
	@SubscribeEvent
	public void onConfigChange(ConfigChangedEvent.OnConfigChangedEvent e) {
		if (e.getModID().equals(BaseMinerals.MODID)) {
			init();
		}
	}

	public static void init() {
		if (configuration == null) {
			configuration = new Configuration(new File(CONFIG_FILE));
			MinecraftForge.EVENT_BUS.register(new BMiConfig());
		}

		configMaterialOptions(MATERIAL_CONFIG_OPTIONS, configuration);
		
		if (configuration.hasChanged()) {
			configuration.save();
		}
	}

	public static void postInit() {
		CrusherRecipeRegistry.getInstance().clearCache();
	}
}
