package com.mcmoddev.baseminerals.integration.plugins;

import com.mcmoddev.baseminerals.integration.BaseMineralsPlugin;
import com.mcmoddev.lib.integration.IIntegration;

/**
 *
 * @author Jasmine Iwanek
 *
 */
@BaseMineralsPlugin(EnderIO.PLUGIN_MODID)
public class EnderIO extends com.mcmoddev.lib.integration.plugins.EnderIO implements IIntegration {

	private static boolean initDone = false;

	/**
	 *
	 */
	@Override
	public void init() {
		if (initDone || !com.mcmoddev.basemetals.util.Config.Options.enableEnderIO) {
			return;
		}


		initDone = true;
	}
}
