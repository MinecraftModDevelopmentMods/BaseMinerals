package zone.moddev.mc.baseminerals.integration.plugins;

import zone.moddev.mc.baseminerals.BaseMinerals;
import zone.moddev.mc.baseminerals.data.MaterialNames;
import zone.moddev.mc.baseminerals.init.Materials;
import com.mcmoddev.lib.integration.MMDPlugin;
import com.mcmoddev.lib.integration.IIntegration;
import com.mcmoddev.lib.material.MMDMaterial;
import com.mcmoddev.lib.util.ConfigBase.Options;

/**
 *
 * @author Jasmine Iwanek
 *
 */
@MMDPlugin(addonId = BaseMinerals.MODID, pluginId = TinkersConstruct.PLUGIN_MODID)
public class TinkersConstruct extends com.mcmoddev.lib.integration.plugins.TinkersConstructBase implements IIntegration {

	@Override
	public void init() {
		if (!Options.isModEnabled(PLUGIN_MODID)) {
			return;
		}

		if (Materials.hasMaterial(MaterialNames.LITHIUM)) {
			final MMDMaterial lithium = Materials.getMaterialByName(MaterialNames.LITHIUM);
			registerFluid(lithium, 144);
            registerCasting(lithium,144);

		}

		if (Materials.hasMaterial(MaterialNames.SILICON)) {
			final MMDMaterial silicon = Materials.getMaterialByName(MaterialNames.SILICON);
			registerFluid(silicon, 144);
            registerCasting(silicon,144);

		}
	}

}
