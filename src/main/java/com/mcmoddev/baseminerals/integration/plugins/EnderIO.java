package com.mcmoddev.baseminerals.integration.plugins;

import com.mcmoddev.baseminerals.BaseMinerals;
import com.mcmoddev.lib.integration.MMDPlugin;
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
		if (initDone || !com.mcmoddev.basemetals.util.Config.Options.modEnabled.get("enderio")) {
			return;
		}

		addSagMillRecipe("lithium", null, 3600);
		addSagMillRecipe("niter", null, 3600);
		addSagMillRecipe("phosphorus", null, 3600);
		addSagMillRecipe("potash", null, 3600);
		addSagMillRecipe("salt", null, 3600);
		addSagMillRecipe("saltpeter", null, 3600);
		// addSagMillRecipe("silicon", null, 3600);
		addSagMillRecipe("sulfur", null, 3600);

		initDone = true;
	}
}
