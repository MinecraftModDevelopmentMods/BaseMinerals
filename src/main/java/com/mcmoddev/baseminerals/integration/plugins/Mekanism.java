package com.mcmoddev.baseminerals.integration.plugins;

import cyano.basemetals.integration.BaseMetalsPlugin;
import cyano.basemetals.integration.IIntegration;

@BaseMetalsPlugin(Mekanism.PLUGIN_MODID)
public class Mekanism extends cyano.basemetals.integration.plugins.Mekanism implements IIntegration {

	private static boolean initDone = false;

	@Override
	public void init() {
		if (initDone || !cyano.basemetals.util.Config.Options.ENABLE_MEKANISM) {
			return;
		}

		initDone = true;
	}
}
