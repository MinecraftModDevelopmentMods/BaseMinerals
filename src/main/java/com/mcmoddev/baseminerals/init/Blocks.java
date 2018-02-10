package com.mcmoddev.baseminerals.init;

import com.mcmoddev.baseminerals.init.ItemGroups;

import java.util.Arrays;
import java.util.List;

import com.mcmoddev.baseminerals.data.MaterialNames;
import com.mcmoddev.lib.data.Names;
import com.mcmoddev.lib.data.SharedStrings;
import com.mcmoddev.lib.init.Materials;
import com.mcmoddev.lib.material.MMDMaterial;

/**
 * This class initializes all blocks in Base Minerals.
 *
 * @author Jasmine Iwanek
 *
 */
public class Blocks extends com.mcmoddev.lib.init.Blocks {

	protected Blocks() {
		throw new IllegalAccessError(SharedStrings.NOT_INSTANTIABLE);
	}

	/**
	 *
	 */
	public static void init() {
/*
		if (Materials.hasMaterial(MaterialNames.CHARCOAL)) {
			create(Names.BLOCK, Materials.getMaterialByName(MaterialNames.CHARCOAL), ItemGroups.myTabs.blocksTab);
		}
*/
		final List<String> materials = Arrays.asList(MaterialNames.CHARCOAL, MaterialNames.LITHIUM, MaterialNames.NITER,
				MaterialNames.PHOSPHORUS, MaterialNames.POTASH, MaterialNames.SALT, MaterialNames.SALTPETER,
				MaterialNames.SILICON, MaterialNames.SULFUR);

		materials.stream().filter(Materials::hasMaterial).forEach(materialName -> {
			final MMDMaterial material = Materials.getMaterialByName(materialName);

			create(Names.BLOCK, material, ItemGroups.myTabs.blocksTab);
			create(Names.ORE, material, ItemGroups.myTabs.blocksTab);
		});
/*
		if (Materials.hasMaterial(MaterialNames.SILICON)) {
			create(Names.BLOCK, Materials.getMaterialByName(MaterialNames.SILICON), ItemGroups.myTabs.blocksTab);
		}
*/
	}
}
