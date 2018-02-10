package com.mcmoddev.baseminerals.init;

import com.mcmoddev.baseminerals.data.MaterialNames;
import com.mcmoddev.baseminerals.init.Materials;
import com.mcmoddev.lib.data.Names;
import com.mcmoddev.lib.data.SharedStrings;
import com.mcmoddev.lib.material.MMDMaterial;
import com.mcmoddev.lib.util.Oredicts;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapelessOreRecipe;

/**
 *
 * @author Jasmine Iwanek
 *
 */
public class Recipes extends com.mcmoddev.lib.init.Recipes {

	private Recipes() {
		throw new IllegalAccessError(SharedStrings.NOT_INSTANTIABLE);
	}

	/**
	 *
	 */
	public static void init() {
		initModSpecificRecipes();
	}

	private static void initModSpecificRecipes() {

		// TODO: Make gunpowder Recipe Configurable
		if ((Materials.hasMaterial(MaterialNames.SULFUR))) {
			final MMDMaterial sulfur = Materials.getMaterialByName(MaterialNames.SULFUR);
			final String dustSulphur = Oredicts.DUST + sulfur.getCapitalizedName();
			final ItemStack gunpowder = new ItemStack(net.minecraft.init.Items.GUNPOWDER, 3);
			if (Materials.hasMaterial(MaterialNames.SALTPETER)) {
				final MMDMaterial saltpeter = Materials.getMaterialByName(MaterialNames.SALTPETER);
				final String dustSaltpeter = Oredicts.DUST + saltpeter.getCapitalizedName();
				GameRegistry.addRecipe(new ShapelessOreRecipe(gunpowder, Oredicts.DUST_COAL, dustSulphur, dustSaltpeter));
				GameRegistry.addRecipe(new ShapelessOreRecipe(gunpowder, Oredicts.DUST_CHARCOAL, dustSulphur, dustSaltpeter));
			}
			if (Materials.hasMaterial(MaterialNames.POTASH)) {
				final MMDMaterial potash = Materials.getMaterialByName(MaterialNames.POTASH);
				final String dustPotash = Oredicts.DUST + potash.getCapitalizedName();
				GameRegistry.addRecipe(new ShapelessOreRecipe(gunpowder, Oredicts.DUST_COAL, dustSulphur, dustPotash));
				GameRegistry.addRecipe(new ShapelessOreRecipe(gunpowder, Oredicts.DUST_CHARCOAL, dustSulphur, dustPotash));
			}
		}

		if (Materials.hasMaterial(MaterialNames.SILICON)) {
			final MMDMaterial silicon = Materials.getMaterialByName(MaterialNames.SILICON);
			if (silicon.hasItem(Names.INGOT)) {
				OreDictionary.registerOre("itemSilicon", silicon.getItem(Names.INGOT));
				if (silicon.hasItem(Names.BLEND)) {
					GameRegistry.addRecipe(new ShapelessOreRecipe(silicon.getItemStack(Names.BLEND, 1), "sand", Oredicts.DUST_COAL));
					GameRegistry.addRecipe(new ShapelessOreRecipe(silicon.getItemStack(Names.BLEND, 1), "sand", Oredicts.DUST_CHARCOAL));
				}
			}
		}
	}
}
