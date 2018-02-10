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

	/**
	 *
	 */
	@Override
	public void init() {
		if (!Options.isModEnabled(PLUGIN_MODID)) {
			return;
		}

		addSagMillRecipe(MaterialNames.LITHIUM, null, 3600);
		addSagMillRecipe(MaterialNames.NITER, null, 3600);
		addSagMillRecipe(MaterialNames.PHOSPHORUS, null, 3600);
		addSagMillRecipe(MaterialNames.POTASH, null, 3600);
		addSagMillRecipe(MaterialNames.SALT, null, 3600);
		addSagMillRecipe(MaterialNames.SALTPETER, null, 3600);
		// addSagMillRecipe(MaterialNames.SILICON, null, 3600);
		addSagMillRecipe(MaterialNames.SULFUR, null, 3600);
	}
}
