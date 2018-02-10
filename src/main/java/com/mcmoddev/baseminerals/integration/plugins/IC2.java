package com.mcmoddev.baseminerals.integration.plugins;

import com.mcmoddev.baseminerals.BaseMinerals;
import com.mcmoddev.lib.integration.MMDPlugin;
import com.mcmoddev.lib.util.ConfigBase.Options;
import com.mcmoddev.lib.integration.IIntegration;

@MMDPlugin(addonId = BaseMinerals.MODID, pluginId = IC2.PLUGIN_MODID)
public class IC2 extends com.mcmoddev.lib.integration.plugins.IC2Base implements IIntegration {

	@Override
	public void init() {
		if (!Options.isModEnabled(PLUGIN_MODID)) {
			return;
		}
	}
}
