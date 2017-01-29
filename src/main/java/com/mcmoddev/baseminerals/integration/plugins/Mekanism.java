package com.mcmoddev.baseminerals.integration.plugins;

import com.mcmoddev.baseminerals.integration.BaseMineralsPlugin;
import com.mcmoddev.basemetals.integration.IIntegration;

@BaseMineralsPlugin(Mekanism.PLUGIN_MODID)
public class Mekanism extends com.mcmoddev.basemetals.integration.plugins.Mekanism implements IIntegration {

	private static boolean initDone = false;

	@Override
	public void init() {
		if (initDone || !com.mcmoddev.basemetals.util.Config.Options.ENABLE_MEKANISM) {
			return;
		}

		initDone = true;
	}
}
