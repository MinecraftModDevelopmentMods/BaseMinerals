package com.mcmoddev.baseminerals.integration.plugins;

import com.mcmoddev.baseminerals.util.Config.Options;

import com.mcmoddev.baseminerals.integration.BaseMineralsPlugin;
import com.mcmoddev.basemetals.integration.IIntegration;

/**
 *
 * @author Jasmine Iwanek
 *
 */
@BaseMineralsPlugin(TinkersConstruct.PLUGIN_MODID)
public class TinkersConstruct extends com.mcmoddev.basemetals.integration.plugins.TinkersConstruct implements IIntegration {

	private static boolean initDone = false;

	@Override
	public void init() {
		if (initDone || !com.mcmoddev.basemetals.util.Config.Options.ENABLE_TINKERS_CONSTRUCT) {
			return;
		}

		if (Options.ENABLE_LITHIUM) {
			//registerMaterial("lithium", false, true);
			//registerFluid(Fluids.fluidLithium, true);
		}
		if (Options.ENABLE_SILICON) {
			//registerMaterial("silicon", false, true);
			//registerFluid(Fluids.fluidSilicon, true);
		}

		initDone = true;
	}
}
