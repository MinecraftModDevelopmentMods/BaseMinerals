package com.mcmoddev.baseminerals.init;

import com.mcmoddev.baseminerals.data.MaterialNames;
import com.mcmoddev.baseminerals.init.Materials;
import com.mcmoddev.baseminerals.util.Config.Options;
import com.mcmoddev.lib.data.Names;
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

	private static boolean initDone = false;

	private Recipes() {
		throw new IllegalAccessError("Not a instantiable class");
	}

	/**
	 *
	 */
	public static void init() {
		if (initDone) {
			return;
		}

		Materials.init();
		Blocks.init();
		Items.init();

		initModSpecificRecipes();

		initDone = true;
	}

	private static void initModSpecificRecipes() {

		if ((Options.materialEnabled(MaterialNames.SULFUR))) {
			if (Options.materialEnabled(MaterialNames.SALTPETER)) {
				GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(net.minecraft.init.Items.GUNPOWDER, 3), Oredicts.DUST_COAL, Oredicts.DUST + "Sulfur", Oredicts.DUST + "Saltpeter"));
				GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(net.minecraft.init.Items.GUNPOWDER, 3), Oredicts.DUST_CHARCOAL, Oredicts.DUST + "Sulfur", Oredicts.DUST + "Saltpeter"));
			}
			if (Options.materialEnabled(MaterialNames.POTASH)) {
				GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(net.minecraft.init.Items.GUNPOWDER, 3), Oredicts.DUST_COAL, Oredicts.DUST + "Sulfur", Oredicts.DUST + "Potash"));
				GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(net.minecraft.init.Items.GUNPOWDER, 3), Oredicts.DUST_CHARCOAL, Oredicts.DUST + "Sulfur", Oredicts.DUST + "Potash"));
			}
		}

		if (Options.materialEnabled(MaterialNames.SILICON)) {
			GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(Materials.getMaterialByName(MaterialNames.SILICON).getItem(Names.POWDER), 1), "sand", Oredicts.DUST_COAL));
			GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(Materials.getMaterialByName(MaterialNames.SILICON).getItem(Names.POWDER), 1), "sand", Oredicts.DUST_CHARCOAL));
			OreDictionary.registerOre("itemSilicon", Materials.getMaterialByName(MaterialNames.SILICON).getItem(Names.INGOT));
		}
	}
}
