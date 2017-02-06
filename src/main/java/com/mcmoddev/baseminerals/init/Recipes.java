package com.mcmoddev.baseminerals.init;

import com.mcmoddev.baseminerals.init.Materials;
import com.mcmoddev.baseminerals.util.Config.Options;

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

		if ((Options.enableSulfur) && (Options.enableSaltpeter)) {
			if (Options.enableSaltpeter) {
				GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(net.minecraft.init.Items.GUNPOWDER, 3), "dustCoal", "dustSulfur", "dustSaltpeter"));
				GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(net.minecraft.init.Items.GUNPOWDER, 3), "dustCharcoal", "dustSulfur", "dustSaltpeter"));
			}
			if (Options.enableSaltpeter) {
				GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(net.minecraft.init.Items.GUNPOWDER, 3), "dustCoal", "dustSulfur", "dustPotash"));
				GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(net.minecraft.init.Items.GUNPOWDER, 3), "dustCharcoal", "dustSulfur", "dustPotash"));
			}
		}

		if (Options.enableSilicon) {
			GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(Materials.silicon.powder, 1), "sand", "dustCoal"));
			GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(Materials.silicon.powder, 1), "sand", "dustCharcoal"));
			OreDictionary.registerOre("itemSilicon", Materials.silicon.ingot);
		}
	}
}
