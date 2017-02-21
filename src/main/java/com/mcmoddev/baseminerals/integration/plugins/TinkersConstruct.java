package com.mcmoddev.baseminerals.integration.plugins;

import com.mcmoddev.baseminerals.util.Config.Options;
import com.mcmoddev.baseminerals.init.Materials;
import com.mcmoddev.baseminerals.integration.BaseMineralsPlugin;
import com.mcmoddev.lib.integration.IIntegration;

/**
 *
 * @author Jasmine Iwanek
 *
 */
@BaseMineralsPlugin(TinkersConstruct.PLUGIN_MODID)
public class TinkersConstruct extends com.mcmoddev.lib.integration.plugins.TinkersConstruct implements IIntegration {

	private static boolean initDone = false;

	@Override
	public void init() {
		if (initDone || !com.mcmoddev.basemetals.util.Config.Options.enableTinkersConstruct) {
			return;
		}

		if (Options.enableLithium) {
			registerFluid(Materials.lithium, 144);
			registerCasting(Materials.lithium, 144);
		}

		if (Options.enableSilicon) {
			registerFluid(Materials.silicon, 144);
			registerCasting(Materials.silicon, 144);
		}

		initDone = true;
	}
}
