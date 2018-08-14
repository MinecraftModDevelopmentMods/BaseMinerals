package com.mcmoddev.baseminerals.integration.plugins;

import com.mcmoddev.baseminerals.BaseMinerals;
import com.mcmoddev.lib.integration.MMDPlugin;
import com.mcmoddev.lib.util.Config.Options;
import com.mcmoddev.lib.integration.IIntegration;

@MMDPlugin(addonId = BaseMinerals.MODID, pluginId = BMiThaumcraft.PLUGIN_MODID)
public class BMiThaumcraft extends com.mcmoddev.lib.integration.plugins.Thaumcraft implements IIntegration {

	@Override
	public void init() {
		if (!Options.isModEnabled(PLUGIN_MODID)) {
			return;
		}
	}
}
