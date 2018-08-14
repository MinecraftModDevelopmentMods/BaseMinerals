package com.mcmoddev.baseminerals.integration.plugins;

import com.mcmoddev.baseminerals.BaseMinerals;
import com.mcmoddev.baseminerals.data.MaterialNames;
import com.mcmoddev.lib.util.Config.Options;
import com.mcmoddev.lib.init.Materials;
import com.mcmoddev.lib.integration.IIntegration;
import com.mcmoddev.lib.integration.MMDPlugin;
import com.mcmoddev.lib.util.Oredicts;

@MMDPlugin(addonId = BaseMinerals.MODID, pluginId = BMiDenseOres.PLUGIN_MODID)
public class BMiDenseOres extends com.mcmoddev.lib.integration.plugins.DenseOres implements IIntegration {

	private static boolean initDone = false;

	@Override
	public void init() {
		if (initDone || !Options.isModEnabled(BMiDenseOres.PLUGIN_MODID)) {
			return;
		}

		registerOres();

		initDone = true;
	}

	/**
	 * Register all ores that are currently known by the materials registry
	 * 
	 * @author Daniel Hazelton &lt;dshadowwolf@gmail.com&gt;
	 */
	private static void registerOres() {
		final String[] baseNames = new String[] {
				MaterialNames.LITHIUM,
				MaterialNames.NITER,
				MaterialNames.PHOSPHORUS,
				MaterialNames.POTASH,
				MaterialNames.SALT,
				MaterialNames.SALTPETER,
				MaterialNames.SULFUR
		};

		for (int i = 0; i < baseNames.length; i++) {
			final String materialName = baseNames[i];
			if (Materials.hasMaterial(materialName)) {
				registerOre(String.format("%s_%s", materialName, Oredicts.ORE), Oredicts.STONE, 0);
			}
		}
	}
}
