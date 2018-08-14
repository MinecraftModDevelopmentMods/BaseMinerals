package com.mcmoddev.baseminerals.integration.plugins;

import com.mcmoddev.baseminerals.BaseMinerals;
import com.mcmoddev.baseminerals.data.MaterialNames;
import com.mcmoddev.lib.integration.MMDPlugin;
import com.mcmoddev.lib.util.Config.Options;
import com.mcmoddev.lib.integration.IIntegration;

/**
 *
 * @author Jasmine Iwanek
 *
 */
@MMDPlugin(addonId = BaseMinerals.MODID, pluginId = BMiEnderIO.PLUGIN_MODID)
public class BMiEnderIO extends com.mcmoddev.lib.integration.plugins.EnderIO implements IIntegration {

	/**
	 *
	 */
	@Override
	public void init() {
		if (!Options.isModEnabled(PLUGIN_MODID)) {
			return;
		}

		final String[] baseNames = new String[] {
				MaterialNames.LITHIUM,
				MaterialNames.NITER,
				MaterialNames.PHOSPHORUS,
				MaterialNames.POTASH,
				MaterialNames.SALT,
				MaterialNames.SALTPETER,
				// MaterialNames.SILICON,
				MaterialNames.SULFUR
		};

		for (int i = 0; i < baseNames.length; i++) {
			final String materialName = baseNames[i];
			if (Options.isMaterialEnabled(materialName)) {
				addSagMillRecipe(materialName, 3600);
			}
		}
	}
}
