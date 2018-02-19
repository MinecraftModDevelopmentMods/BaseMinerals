package com.mcmoddev.baseminerals.init;

import com.mcmoddev.baseminerals.data.MaterialNames;
import com.mcmoddev.lib.data.Names;
import com.mcmoddev.lib.data.SharedStrings;
import com.mcmoddev.lib.material.MMDMaterial;

import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;

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
		if (Materials.hasMaterial(MaterialNames.SILICON)) {

			final MMDMaterial silicon = Materials.getMaterialByName(MaterialNames.SILICON);
			if (silicon.hasItem(Names.INGOT)) {
				OreDictionary.registerOre("itemSilicon", silicon.getItem(Names.INGOT));
				if (silicon.hasItem(Names.POWDER)) {
					GameRegistry.addSmelting(silicon.getItemStack(Names.POWDER), silicon.getItemStack(Names.INGOT), 0);
				}
			}
		}
	}
}
