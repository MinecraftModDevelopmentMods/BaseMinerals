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
			//registerMaterial(Materials.lithium.getName(), false, true);
			registerFluid(Materials.lithium.fluid, false);
		}
		if (Options.enableSilicon) {
			//registerMaterial(Materials.silicon.getName(), false, true);
			registerFluid(Materials.silicon.fluid, false);
		}

		initDone = true;
	}
}
