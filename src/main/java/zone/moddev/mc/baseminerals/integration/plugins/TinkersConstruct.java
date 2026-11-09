package zone.moddev.mc.baseminerals.integration.plugins;

import zone.moddev.mc.baseminerals.BaseMinerals;
import zone.moddev.mc.baseminerals.data.MaterialNames;
import zone.moddev.mc.baseminerals.init.Materials;
import com.mcmoddev.lib.data.Names;
import com.mcmoddev.lib.integration.MMDPlugin;
import com.mcmoddev.lib.integration.IIntegration;
import com.mcmoddev.lib.material.MMDMaterial;
import com.mcmoddev.lib.util.ConfigBase.Options;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import slimeknights.tconstruct.library.TinkerRegistry;

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

		registerMoltenForms(MaterialNames.LITHIUM);
		registerMoltenForms(MaterialNames.SILICON);
	}

	private static void registerMoltenForms(String name) {
		if (!Materials.hasMaterial(name)) {
			return;
		}
		final MMDMaterial material = Materials.getMaterialByName(name);
		final Fluid fluid = material.getFluid();
		if (fluid == null) {
			return;
		}
		final int ingotAmount = 144;
		registerFluid(material, ingotAmount);
		// Tinkers on 1.11 represents an empty table or basin with ItemStack.EMPTY.
		if (material.hasBlock(Names.BLOCK)) {
			TinkerRegistry.registerBasinCasting(material.getBlockItemStack(Names.BLOCK), ItemStack.EMPTY, fluid, ingotAmount * 9);
		}
		if (material.hasItem(Names.INGOT)) {
			TinkerRegistry.registerTableCasting(material.getItemStack(Names.INGOT), ItemStack.EMPTY, fluid, ingotAmount);
		}
		if (material.hasItem(Names.NUGGET)) {
			TinkerRegistry.registerTableCasting(material.getItemStack(Names.NUGGET), ItemStack.EMPTY, fluid, ingotAmount / 9);
		}
	}
}
