package com.mcmoddev.baseminerals.integration.plugins;

import java.util.Arrays;

import javax.annotation.Nonnull;

import com.mcmoddev.basemetals.init.Materials;
import com.mcmoddev.baseminerals.BaseMinerals;
import com.mcmoddev.baseminerals.data.MaterialNames;
import com.mcmoddev.lib.integration.MMDPlugin;
import com.mcmoddev.lib.material.MMDMaterial;
import com.mcmoddev.lib.util.ConfigBase.Options;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import com.mcmoddev.lib.data.Names;
import com.mcmoddev.lib.integration.IIntegration;

@MMDPlugin(addonId = BaseMinerals.MODID, pluginId = Mekanism.PLUGIN_MODID)
public class Mekanism extends com.mcmoddev.lib.integration.plugins.MekanismBase implements IIntegration {

	@Override
	public void init() {
		if (!Options.isModEnabled(PLUGIN_MODID)) {
			return;
		}
		MinecraftForge.EVENT_BUS.register(this);
	}
	
	private boolean doesNotHaveMaterial(@Nonnull final String name) {
		return !Materials.hasMaterial(name);
	}
	
	@SubscribeEvent
	public void regCallback(RegistryEvent.Register<IRecipe> event) {
		Arrays.asList(MaterialNames.LITHIUM, MaterialNames.NITER, MaterialNames.PHOSPHORUS, 
				MaterialNames.POTASH, MaterialNames.SALT, MaterialNames.SALTPETER, MaterialNames.SULFUR)
		.stream()
		.filter(this::doesNotHaveMaterial)
		.forEach(this::regMek);
	}
	
	private void regMek(@Nonnull final String name) {
		final MMDMaterial mat = Materials.getMaterialByName(name);
		final ItemStack ore = mat.getBlockItemStack(Names.ORE);
		final ItemStack powder = mat.getItemStack(Names.POWDER, 6);

		if ((!ore.isEmpty()) && (!powder.isEmpty()))
			super.addEnrichmentChamberRecipe(ore, powder);
	}
}
