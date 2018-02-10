package com.mcmoddev.baseminerals.integration.plugins;

import com.mcmoddev.baseminerals.BaseMinerals;
import com.mcmoddev.lib.integration.MMDPlugin;
import com.mcmoddev.lib.util.ConfigBase.Options;
import com.mcmoddev.lib.integration.IIntegration;

@MMDPlugin(addonId = BaseMinerals.MODID, pluginId = Thaumcraft.PLUGIN_MODID)
public class Thaumcraft extends com.mcmoddev.lib.integration.plugins.ThaumcraftBase implements IIntegration {

	@Override
	public void init() {
		if (!Options.isModEnabled(PLUGIN_MODID)) {
			return;
		}
	}
}
