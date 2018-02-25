package com.mcmoddev.baseminerals.integration.plugins;

import java.util.Arrays;

import com.mcmoddev.baseminerals.BaseMinerals;
import com.mcmoddev.baseminerals.data.MaterialNames;
import com.mcmoddev.baseminerals.init.Materials;
import com.mcmoddev.lib.integration.MMDPlugin;
import com.mcmoddev.lib.integration.IIntegration;
import com.mcmoddev.lib.material.MMDMaterial;
import com.mcmoddev.lib.util.ConfigBase.Options;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.event.FMLInterModComms;

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

		Arrays.asList(MaterialNames.LITHIUM, MaterialNames.SILICON).forEach(TinkersConstruct::doIMC);
	}
	
	private static void doIMC(String materialName) {
		final MMDMaterial material = Materials.getMaterialByName(materialName);
		if(material.isEmpty()) return;
		
		// create NBT for the IMC
		NBTTagCompound tag = new NBTTagCompound();
		tag.setString("fluid", material.getFluid().getName());
		tag.setString("ore", material.getCapitalizedName()); 
		tag.setBoolean("toolforge", false); 
		FMLInterModComms.sendMessage(PLUGIN_MODID, "integrateSmeltery", tag);
	}
}
