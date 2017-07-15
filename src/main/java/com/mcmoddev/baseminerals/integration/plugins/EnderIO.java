package com.mcmoddev.baseminerals.integration.plugins;

import com.mcmoddev.baseminerals.BaseMinerals;
import com.mcmoddev.baseminerals.data.MaterialNames;
import com.mcmoddev.lib.integration.MMDPlugin;
import com.mcmoddev.lib.util.ConfigBase.Options;
import com.mcmoddev.lib.integration.IIntegration;

/**
 *
 * @author Jasmine Iwanek
 *
 */
@MMDPlugin(addonId = BaseMinerals.MODID, pluginId = EnderIO.PLUGIN_MODID)
public class EnderIO extends com.mcmoddev.lib.integration.plugins.EnderIOBase implements IIntegration {

	private static boolean initDone = false;

	/**
	 *
	 */
	@Override
	public void init() {
		if (initDone || !Options.isModEnabled(EnderIO.PLUGIN_MODID)) {
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

		initDone = true;
	}
}
