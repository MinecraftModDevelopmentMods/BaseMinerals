package com.mcmoddev.baseminerals.init;

import com.mcmoddev.basemetals.init.ItemGroups;
import com.mcmoddev.baseminerals.util.Config.Options;
import com.mcmoddev.lib.fuels.FuelRegistry;
import com.mcmoddev.lib.util.Oredicts;
import com.mcmoddev.lib.util.TabContainer;

/**
 * This class initializes all items in Base Minerals.
 *
 * @author Jasmine Iwanek
 *
 */
public class Items extends com.mcmoddev.lib.init.Items {

	private static boolean initDone = false;
	private static TabContainer myTabs = new TabContainer( ItemGroups.blocksTab, ItemGroups.itemsTab, null );

	protected Items() {
		throw new IllegalAccessError("Not a instantiable class");
	}

	/**
	 *
	 */
	public static void init() {
		if (initDone) {
			return;
		}

		Blocks.init();

		if (Options.enableCharcoal) {
			createNugget(Materials.vanilla_charcoal, myTabs.itemsTab); // Special
			createPowder(Materials.vanilla_charcoal, myTabs.itemsTab);
			createSmallPowder(Materials.vanilla_charcoal, myTabs.itemsTab);
			FuelRegistry.addFuel(Oredicts.NUGGET_CHARCOAL, 200);
			FuelRegistry.addFuel(Oredicts.DUST_CHARCOAL, 1600);
			FuelRegistry.addFuel(Oredicts.DUST_SMALL_CHARCOAL, 200);
			FuelRegistry.addFuel(Oredicts.DUST_TINY_CHARCOAL, 200);
			FuelRegistry.addFuel(Oredicts.BLOCK+"Charcoal", 16000);
		}

		if (Options.enableLithium) {
			createIngot(Materials.getMaterialByName("lithium"), myTabs.itemsTab);
			createNugget(Materials.getMaterialByName("lithium"), myTabs.itemsTab);
			createPowder(Materials.getMaterialByName("lithium"), myTabs.itemsTab);
			createSmallPowder(Materials.getMaterialByName("lithium"), myTabs.itemsTab);
		}

		if (Options.enableNiter) {
			createPowder(Materials.getMaterialByName("niter"), myTabs.itemsTab);
			createSmallPowder(Materials.getMaterialByName("niter"), myTabs.itemsTab);
		}

		if (Options.enablePhosphorus) {
			createPowder(Materials.getMaterialByName("phosphorus"), myTabs.itemsTab);
			createSmallPowder(Materials.getMaterialByName("phosphorus"), myTabs.itemsTab);
		}

		if (Options.enablePotash) {
			createPowder(Materials.getMaterialByName("potash"), myTabs.itemsTab);
			createSmallPowder(Materials.getMaterialByName("potash"), myTabs.itemsTab);
		}

		if (Options.enableSalt) {
			createPowder(Materials.getMaterialByName("salt"), myTabs.itemsTab);
			createSmallPowder(Materials.getMaterialByName("salt"), myTabs.itemsTab);
		}

		if (Options.enableSaltpeter) {
			createPowder(Materials.getMaterialByName("saltpeter"), myTabs.itemsTab);
			createSmallPowder(Materials.getMaterialByName("saltpeter"), myTabs.itemsTab);
		}

		if (Options.enableSilicon) {
			createBlend(Materials.getMaterialByName("silicon"), myTabs.itemsTab);
			createIngot(Materials.getMaterialByName("silicon"), myTabs.itemsTab);
			createNugget(Materials.getMaterialByName("silicon"), myTabs.itemsTab);
			createPowder(Materials.getMaterialByName("silicon"), myTabs.itemsTab);
			createSmallBlend(Materials.getMaterialByName("silicon"), myTabs.itemsTab);
			createSmallPowder(Materials.getMaterialByName("silicon"), myTabs.itemsTab);
		}

		if (Options.enableSulfur) {
			createPowder(Materials.getMaterialByName("sulfur"), myTabs.itemsTab);
			createSmallPowder(Materials.getMaterialByName("sulfur"), myTabs.itemsTab);
		}

		// addToMetList() // May not be needed, check

		initDone = true;
	}
}
