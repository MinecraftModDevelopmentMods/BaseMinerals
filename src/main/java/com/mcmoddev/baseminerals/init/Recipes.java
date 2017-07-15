package com.mcmoddev.baseminerals.init;

import org.apache.commons.lang3.StringUtils;

import com.mcmoddev.baseminerals.data.MaterialNames;
import com.mcmoddev.baseminerals.init.Materials;
import com.mcmoddev.lib.util.ConfigBase.Options;
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
		final String sulfurCapitalized = StringUtils.capitalize(MaterialNames.SULFUR);
		final String saltpeterCapitalized = StringUtils.capitalize(MaterialNames.SALTPETER);
		final String potashCapitalized = StringUtils.capitalize(MaterialNames.POTASH);

		// TODO: Make gunpowder Recipe Configurable
		if ((Options.isMaterialEnabled(MaterialNames.SULFUR))) {
			if (Options.isMaterialEnabled(MaterialNames.SALTPETER)) {
				GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(net.minecraft.init.Items.GUNPOWDER, 3), Oredicts.DUST_COAL, Oredicts.DUST + sulfurCapitalized, Oredicts.DUST + saltpeterCapitalized));
				GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(net.minecraft.init.Items.GUNPOWDER, 3), Oredicts.DUST_CHARCOAL, Oredicts.DUST + sulfurCapitalized, Oredicts.DUST + saltpeterCapitalized));
			}
			if (Options.isMaterialEnabled(MaterialNames.POTASH)) {
				GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(net.minecraft.init.Items.GUNPOWDER, 3), Oredicts.DUST_COAL, Oredicts.DUST + sulfurCapitalized, Oredicts.DUST + potashCapitalized));
				GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(net.minecraft.init.Items.GUNPOWDER, 3), Oredicts.DUST_CHARCOAL, Oredicts.DUST + sulfurCapitalized, Oredicts.DUST + potashCapitalized));
			}
		}

		if (Options.isMaterialEnabled(MaterialNames.SILICON)) {
			GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(Materials.getMaterialByName(MaterialNames.SILICON).getItem(Names.POWDER), 1), Oredicts.SAND, Oredicts.DUST_COAL));
			GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(Materials.getMaterialByName(MaterialNames.SILICON).getItem(Names.POWDER), 1), Oredicts.SAND, Oredicts.DUST_CHARCOAL));
			OreDictionary.registerOre("itemSilicon", Materials.getMaterialByName(MaterialNames.SILICON).getItem(Names.INGOT));
		}
	}
}
