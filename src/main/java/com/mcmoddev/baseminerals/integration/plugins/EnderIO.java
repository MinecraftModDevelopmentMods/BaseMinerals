package com.mcmoddev.baseminerals.integration.plugins;

import cyano.basemetals.integration.BaseMetalsPlugin;
import cyano.basemetals.integration.IIntegration;

/**
 *
 * @author Jasmine Iwanek
 *
 */
@BaseMetalsPlugin(EnderIO.PLUGIN_MODID)
public class EnderIO extends cyano.basemetals.integration.plugins.EnderIO implements IIntegration {

	private static boolean initDone = false;

	/**
	 *
	 */
	@Override
	public void init() {
		if (initDone || !cyano.basemetals.util.Config.Options.ENABLE_ENDER_IO) {
			return;
		}

		// TODO: Verify blocks, Slabs, Ingots, Oreblocks

		initDone = true;
	}
}
