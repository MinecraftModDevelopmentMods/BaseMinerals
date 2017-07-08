package com.mcmoddev.baseminerals.integration.plugins;

import com.mcmoddev.baseminerals.BaseMinerals;
import com.mcmoddev.baseminerals.data.MaterialNames;
import com.mcmoddev.baseminerals.init.Materials;
import com.mcmoddev.lib.util.ConfigBase.Options;
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
		if (initDone || !Options.isModEnabled("tinkersconstruct")) {
			return;
		}

		if (Options.isMaterialEnabled("Lithium")) {
			registerFluid(Materials.getMaterialByName(MaterialNames.LITHIUM), 144);
			registerCasting(Materials.getMaterialByName(MaterialNames.LITHIUM), 144);
		}

		if (Options.isMaterialEnabled("Silicon")) {
			registerFluid(Materials.getMaterialByName(MaterialNames.SILICON), 144);
			registerCasting(Materials.getMaterialByName(MaterialNames.SILICON), 144);
		}

		initDone = true;
	}
}
