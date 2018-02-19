package com.mcmoddev.baseminerals.integration.plugins;

import com.mcmoddev.baseminerals.BaseMinerals;
import com.mcmoddev.baseminerals.data.MaterialNames;
import com.mcmoddev.lib.util.ConfigBase.Options;
import com.mcmoddev.lib.init.Materials;
import com.mcmoddev.lib.integration.IIntegration;
import com.mcmoddev.lib.integration.MMDPlugin;
import com.mcmoddev.lib.material.MMDMaterial;
import com.mcmoddev.lib.util.Oredicts;

@MMDPlugin(addonId = BaseMinerals.MODID, pluginId = DenseOres.PLUGIN_MODID)
public class DenseOres extends com.mcmoddev.lib.integration.plugins.DenseOresBase implements IIntegration {

	private static boolean initDone = false;

	@Override
	public void init() {
		if (initDone || !Options.isModEnabled(DenseOres.PLUGIN_MODID)) {
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
			final MMDMaterial material = Materials.getMaterialByName(materialName);
			if (material != null && Options.isMaterialEnabled(materialName)) {
				registerOre(String.format("%s_%s", materialName, Oredicts.ORE), Oredicts.STONE, 0);
			}
		}
	}
}
