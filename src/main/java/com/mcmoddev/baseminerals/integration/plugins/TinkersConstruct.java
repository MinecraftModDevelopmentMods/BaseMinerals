package com.mcmoddev.baseminerals.integration.plugins;

import com.mcmoddev.baseminerals.BaseMinerals;
import com.mcmoddev.baseminerals.data.MaterialNames;
import com.mcmoddev.baseminerals.init.Materials;
import com.mcmoddev.baseminerals.util.Config.Options;
import com.mcmoddev.lib.integration.MMDPlugin;
import com.mcmoddev.lib.integration.IIntegration;

/**
 *
 * @author Jasmine Iwanek
 *
 */
@MMDPlugin(addonId = BaseMinerals.MODID, pluginId = TinkersConstruct.PLUGIN_MODID)
public class TinkersConstruct extends com.mcmoddev.lib.integration.plugins.TinkersConstructBase implements IIntegration {

	private static boolean initDone = false;

	@Override
	public void init() {
		if (initDone || !com.mcmoddev.basemetals.util.Config.Options.modEnabled("tinkersconstruct")) {
			return;
		}

		if (Options.enableLithium) {
			registerFluid(Materials.getMaterialByName(MaterialNames.LITHIUM), 144);
			registerCasting(Materials.getMaterialByName(MaterialNames.LITHIUM), 144);
		}

		if (Options.enableSilicon) {
			registerFluid(Materials.getMaterialByName(MaterialNames.SILICON), 144);
			registerCasting(Materials.getMaterialByName(MaterialNames.SILICON), 144);
		}

		initDone = true;
	}
}
