package com.mcmoddev.baseminerals.integration.plugins;

import com.mcmoddev.baseminerals.BaseMinerals;
import com.mcmoddev.baseminerals.data.MaterialNames;
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
		if (initDone || !com.mcmoddev.basemetals.util.Config.Options.modEnabled(TinkersConstruct.PLUGIN_MODID)) {
			return;
		}

		if (Options.materialEnabled(MaterialNames.LITHIUM)) {
			registerFluid(MaterialNames.LITHIUM, 144);
			registerCasting(MaterialNames.LITHIUM, 144);
		}

		if (Options.materialEnabled(MaterialNames.SILICON)) {
			registerFluid(MaterialNames.SILICON, 144);
			registerCasting(MaterialNames.SILICON, 144);
		}

		initDone = true;
	}
}
