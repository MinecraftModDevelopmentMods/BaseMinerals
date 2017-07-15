package com.mcmoddev.baseminerals.init;

import com.mcmoddev.baseminerals.init.ItemGroups;
import com.mcmoddev.baseminerals.data.MaterialNames;
import com.mcmoddev.lib.util.ConfigBase.Options;
import com.mcmoddev.lib.data.Names;
import com.mcmoddev.lib.material.MMDMaterial;

/**
 * This class initializes all blocks in Base Minerals.
 *
 * @author Jasmine Iwanek
 *
 */
public class Blocks extends com.mcmoddev.lib.init.Blocks {

	private static boolean initDone = false;

	protected Blocks() {
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
		ItemGroups.init();

		if (Options.isMaterialEnabled(MaterialNames.CHARCOAL)) {
			create(Names.BLOCK, MaterialNames.CHARCOAL, ItemGroups.myTabs.blocksTab);
		}

		if (Options.isMaterialEnabled(MaterialNames.LITHIUM)) {
			final MMDMaterial material = Materials.getMaterialByName(MaterialNames.LITHIUM);
			create(Names.BLOCK, material, ItemGroups.myTabs.blocksTab);
			create(Names.ORE, material, ItemGroups.myTabs.blocksTab);
		}

		if (Options.isMaterialEnabled(MaterialNames.NITER)) {
			final MMDMaterial material = Materials.getMaterialByName(MaterialNames.NITER);
			create(Names.BLOCK, material, ItemGroups.myTabs.blocksTab);
			create(Names.ORE, material, ItemGroups.myTabs.blocksTab);
		}

		if (Options.isMaterialEnabled(MaterialNames.PHOSPHORUS)) {
			final MMDMaterial material = Materials.getMaterialByName(MaterialNames.PHOSPHORUS);
			create(Names.BLOCK, material, ItemGroups.myTabs.blocksTab);
			create(Names.ORE, material, ItemGroups.myTabs.blocksTab);
		}

		if (Options.isMaterialEnabled(MaterialNames.POTASH)) {
			final MMDMaterial material = Materials.getMaterialByName(MaterialNames.POTASH);
			create(Names.BLOCK, material, ItemGroups.myTabs.blocksTab);
			create(Names.ORE, material, ItemGroups.myTabs.blocksTab);
		}

		if (Options.isMaterialEnabled(MaterialNames.SALT)) {
			final MMDMaterial material = Materials.getMaterialByName(MaterialNames.SALT);
			create(Names.BLOCK, material, ItemGroups.myTabs.blocksTab);
			create(Names.ORE, material, ItemGroups.myTabs.blocksTab);
		}

		if (Options.isMaterialEnabled(MaterialNames.SALTPETER)) {
			final MMDMaterial material = Materials.getMaterialByName(MaterialNames.SALTPETER);
			create(Names.BLOCK, material, ItemGroups.myTabs.blocksTab);
			create(Names.ORE, material, ItemGroups.myTabs.blocksTab);
		}

		if (Options.isMaterialEnabled(MaterialNames.SILICON)) {
			create(Names.BLOCK, MaterialNames.SILICON, ItemGroups.myTabs.blocksTab);
		}

		if (Options.isMaterialEnabled(MaterialNames.SULFUR)) {
			final MMDMaterial material = Materials.getMaterialByName(MaterialNames.SULFUR);
			create(Names.BLOCK, material, ItemGroups.myTabs.blocksTab);
			create(Names.ORE, material, ItemGroups.myTabs.blocksTab);
		}

		initDone = true;
	}
}
