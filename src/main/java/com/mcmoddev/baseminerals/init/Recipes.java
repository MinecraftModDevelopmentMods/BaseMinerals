package com.mcmoddev.baseminerals.init;

import com.mcmoddev.baseminerals.init.Materials;
import com.mcmoddev.baseminerals.util.Config.Options;
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

		if ((Options.enableSulfur)) {
			if (Options.enableSaltpeter) {
				GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(net.minecraft.init.Items.GUNPOWDER, 3), Oredicts.DUSTCOAL, Oredicts.DUST + "Sulfur", Oredicts.DUST + "Saltpeter"));
				GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(net.minecraft.init.Items.GUNPOWDER, 3), Oredicts.DUSTCHARCOAL, Oredicts.DUST + "Sulfur", Oredicts.DUST + "Saltpeter"));
			}
			if (Options.enablePotash) {
				GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(net.minecraft.init.Items.GUNPOWDER, 3), Oredicts.DUSTCOAL, Oredicts.DUST + "Sulfur", Oredicts.DUST + "Potash"));
				GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(net.minecraft.init.Items.GUNPOWDER, 3), Oredicts.DUSTCHARCOAL, Oredicts.DUST + "Sulfur", Oredicts.DUST + "Potash"));
			}
		}

		if (Options.enableSilicon) {
			GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(Materials.silicon.powder, 1), "sand", Oredicts.DUSTCOAL));
			GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(Materials.silicon.powder, 1), "sand", Oredicts.DUSTCHARCOAL));
			OreDictionary.registerOre("itemSilicon", Materials.silicon.ingot);
		}
	}
}
