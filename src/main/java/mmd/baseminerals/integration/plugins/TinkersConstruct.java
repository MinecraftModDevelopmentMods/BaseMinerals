package mmd.baseminerals.integration.plugins;

import cyano.basemetals.integration.BaseMetalsPlugin;
import cyano.basemetals.integration.IIntegration;
import mmd.baseminerals.util.Config.Options;

/**
 *
 * @author Jasmine Iwanek
 *
 */
@BaseMetalsPlugin(TinkersConstruct.PLUGIN_MODID)
public class TinkersConstruct extends cyano.basemetals.integration.plugins.TinkersConstruct implements IIntegration {

	private static boolean initDone = false;

	@Override
	public void init() {
		if (initDone || !cyano.basemetals.util.Config.Options.ENABLE_TINKERS_CONSTRUCT) {
			return;
		}

		if (Options.ENABLE_LITHIUM) {
			//final Material lithium = new Material("lithium", 0xFFC5C8C1);
			//registerFluid(Fluids.fluidLithium, true);
		}
		if (Options.ENABLE_SILICON) {
			//final Material silicon = new Material("silicon", 0xFFC5C8C1);
			//registerFluid(Fluids.fluidSilicon, true);
		}

		initDone = true;
	}
}
