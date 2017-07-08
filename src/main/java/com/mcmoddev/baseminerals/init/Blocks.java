package com.mcmoddev.baseminerals.init;

import com.mcmoddev.baseminerals.init.ItemGroups;
import com.mcmoddev.baseminerals.data.MaterialNames;
import com.mcmoddev.lib.util.ConfigBase.Options;
import com.mcmoddev.lib.data.Names;
import com.mcmoddev.lib.util.TabContainer;

/**
 * This class initializes all blocks in Base Minerals.
 *
 * @author Jasmine Iwanek
 *
 */
public class Blocks extends com.mcmoddev.lib.init.Blocks {

	private static boolean initDone = false;
	private static TabContainer myTabs = new TabContainer( ItemGroups.blocksTab, ItemGroups.itemsTab, null);

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

		if (Options.isMaterialEnabled("Charcoal")) {
			create(Names.BLOCK, Materials.getMaterialByName(MaterialNames.CHARCOAL), myTabs.blocksTab);
		}

		if (Options.isMaterialEnabled("Lithium")) {
			create(Names.BLOCK, Materials.getMaterialByName(MaterialNames.LITHIUM), myTabs.blocksTab);
			create(Names.ORE, Materials.getMaterialByName(MaterialNames.LITHIUM), myTabs.blocksTab);
		}

		if (Options.isMaterialEnabled("Niter")) {
			create(Names.BLOCK, Materials.getMaterialByName(MaterialNames.NITER), myTabs.blocksTab);
			create(Names.ORE, Materials.getMaterialByName(MaterialNames.NITER), myTabs.blocksTab);
		}

		if (Options.isMaterialEnabled("Phosphorus")) {
			create(Names.BLOCK, Materials.getMaterialByName(MaterialNames.PHOSPHORUS), myTabs.blocksTab);
			create(Names.ORE, Materials.getMaterialByName(MaterialNames.PHOSPHORUS), myTabs.blocksTab);
		}

		if (Options.isMaterialEnabled("Potash")) {
			create(Names.BLOCK, Materials.getMaterialByName(MaterialNames.POTASH), myTabs.blocksTab);
			create(Names.ORE, Materials.getMaterialByName(MaterialNames.POTASH), myTabs.blocksTab);
		}

		if (Options.isMaterialEnabled("Salt")) {
			create(Names.BLOCK, Materials.getMaterialByName(MaterialNames.SALT), myTabs.blocksTab);
			create(Names.ORE, Materials.getMaterialByName(MaterialNames.SALT), myTabs.blocksTab);
		}

		if (Options.isMaterialEnabled("Saltpeter")) {
			create(Names.BLOCK, Materials.getMaterialByName(MaterialNames.SALTPETER), myTabs.blocksTab);
			create(Names.ORE, Materials.getMaterialByName(MaterialNames.SALTPETER), myTabs.blocksTab);
		}

		if (Options.isMaterialEnabled("Silicon")) {
			create(Names.BLOCK, Materials.getMaterialByName(MaterialNames.SILICON), myTabs.blocksTab);
		}

		if (Options.isMaterialEnabled("Sulfur")) {
			create(Names.BLOCK, Materials.getMaterialByName(MaterialNames.SULFUR), myTabs.blocksTab);
			create(Names.ORE, Materials.getMaterialByName(MaterialNames.SULFUR), myTabs.blocksTab);
		}

		initDone = true;
	}
}