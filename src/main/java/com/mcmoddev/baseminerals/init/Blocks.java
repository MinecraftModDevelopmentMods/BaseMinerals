package com.mcmoddev.baseminerals.init;

import com.mcmoddev.basemetals.init.ItemGroups;
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
			createBlock(Materials.vanilla_charcoal, myTabs.blocksTab);
		}

		if (Options.enableLithium) {
			createBlock(Materials.lithium, myTabs.blocksTab);
			createOre(Materials.lithium, myTabs.blocksTab);
		}

		if (Options.enableNiter) {
			createBlock(Materials.niter, myTabs.blocksTab);
			createOre(Materials.niter, myTabs.blocksTab);
		}

		if (Options.enablePhosphorus) {
			createBlock(Materials.phosphorus, myTabs.blocksTab);
			createOre(Materials.phosphorus, myTabs.blocksTab);
		}

		if (Options.enablePotash) {
			createBlock(Materials.potash, myTabs.blocksTab);
			createOre(Materials.potash, myTabs.blocksTab);
		}

		if (Options.enableSalt) {
			createBlock(Materials.salt, myTabs.blocksTab);
			createOre(Materials.salt, myTabs.blocksTab);
		}

		if (Options.enableSaltpeter) {
			createBlock(Materials.saltpeter, myTabs.blocksTab);
			createOre(Materials.saltpeter, myTabs.blocksTab);
		}

		if (Options.enableSilicon) {
			createBlock(Materials.silicon, myTabs.blocksTab);
		}

		if (Options.enableSulfur) {
			createBlock(Materials.sulfur, myTabs.blocksTab);
			createOre(Materials.sulfur, myTabs.blocksTab);
		}

		initDone = true;
	}
}
