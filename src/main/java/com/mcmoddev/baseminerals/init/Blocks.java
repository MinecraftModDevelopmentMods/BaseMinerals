package com.mcmoddev.baseminerals.init;

import com.mcmoddev.baseminerals.init.ItemGroups;
import com.mcmoddev.baseminerals.data.MaterialNames;
import com.mcmoddev.baseminerals.util.Config.Options;
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

		if (Options.enableCharcoal) {
			createBlock(Materials.getMaterialByName(MaterialNames.CHARCOAL), myTabs.blocksTab);
		}

		if (Options.enableLithium) {
			createBlock(Materials.getMaterialByName(MaterialNames.LITHIUM), myTabs.blocksTab);
			createOre(Materials.getMaterialByName(MaterialNames.LITHIUM), myTabs.blocksTab);
		}

		if (Options.enableNiter) {
			createBlock(Materials.getMaterialByName(MaterialNames.NITER), myTabs.blocksTab);
			createOre(Materials.getMaterialByName(MaterialNames.NITER), myTabs.blocksTab);
		}

		if (Options.enablePhosphorus) {
			createBlock(Materials.getMaterialByName(MaterialNames.PHOSPHORUS), myTabs.blocksTab);
			createOre(Materials.getMaterialByName(MaterialNames.PHOSPHORUS), myTabs.blocksTab);
		}

		if (Options.enablePotash) {
			createBlock(Materials.getMaterialByName(MaterialNames.POTASH), myTabs.blocksTab);
			createOre(Materials.getMaterialByName(MaterialNames.POTASH), myTabs.blocksTab);
		}

		if (Options.enableSalt) {
			createBlock(Materials.getMaterialByName(MaterialNames.SALT), myTabs.blocksTab);
			createOre(Materials.getMaterialByName(MaterialNames.SALT), myTabs.blocksTab);
		}

		if (Options.enableSaltpeter) {
			createBlock(Materials.getMaterialByName(MaterialNames.SALTPETER), myTabs.blocksTab);
			createOre(Materials.getMaterialByName(MaterialNames.SALTPETER), myTabs.blocksTab);
		}

		if (Options.enableSilicon) {
			createBlock(Materials.getMaterialByName(MaterialNames.SILICON), myTabs.blocksTab);
		}

		if (Options.enableSulfur) {
			createBlock(Materials.getMaterialByName(MaterialNames.SULFUR), myTabs.blocksTab);
			createOre(Materials.getMaterialByName(MaterialNames.SULFUR), myTabs.blocksTab);
		}

		initDone = true;
	}
}