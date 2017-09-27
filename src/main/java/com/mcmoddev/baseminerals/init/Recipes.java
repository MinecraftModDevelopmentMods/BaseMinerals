package com.mcmoddev.baseminerals.init;

import com.mcmoddev.baseminerals.data.MaterialNames;
import com.mcmoddev.baseminerals.init.Materials;
import com.mcmoddev.lib.util.ConfigBase.Options;
import com.mcmoddev.lib.data.Names;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;

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
		if (Options.isMaterialEnabled(MaterialNames.SILICON)) {
			OreDictionary.registerOre("itemSilicon", Materials.getMaterialByName(MaterialNames.SILICON).getItem(Names.INGOT));
			GameRegistry.addSmelting(Materials.getMaterialByName(MaterialNames.SILICON).getItem(Names.POWDER), new ItemStack(Materials.getMaterialByName(MaterialNames.SILICON).getItem(Names.INGOT)), 0);
		}
	}
}
