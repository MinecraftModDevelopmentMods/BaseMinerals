package com.mcmoddev.baseminerals.init;

import com.mcmoddev.baseminerals.util.Config.Options;
import com.mcmoddev.lib.fuels.FuelRegistry;
import com.mcmoddev.lib.util.Oredicts;
import com.mcmoddev.lib.util.TabContainer;
import com.mcmoddev.baseminerals.data.MaterialNames;

/**
 * This class initializes all items in Base Minerals.
 *
 * @author Jasmine Iwanek
 *
 */
public class Items extends com.mcmoddev.lib.init.Items {

	private static boolean initDone = false;
	private static TabContainer myTabs = ItemGroups.myTabs;

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

		if (Options.materialEnabled(MaterialNames.CHARCOAL)) {
			createNugget(Materials.getMaterialByName(MaterialNames.CHARCOAL), myTabs.itemsTab); // Special
			createPowder(Materials.getMaterialByName(MaterialNames.CHARCOAL), myTabs.itemsTab);
			createSmallPowder(Materials.getMaterialByName(MaterialNames.CHARCOAL), myTabs.itemsTab);
			FuelRegistry.addFuel(Oredicts.NUGGET_CHARCOAL, 200);
			FuelRegistry.addFuel(Oredicts.DUST_CHARCOAL, 1600);
			FuelRegistry.addFuel(Oredicts.DUST_SMALL_CHARCOAL, 200);
			FuelRegistry.addFuel(Oredicts.DUST_TINY_CHARCOAL, 200);
			FuelRegistry.addFuel(Oredicts.BLOCK+"Charcoal", 16000);
		}

		if (Options.materialEnabled(MaterialNames.LITHIUM)) {
			createIngot(Materials.getMaterialByName(MaterialNames.LITHIUM), myTabs.itemsTab);
			createNugget(Materials.getMaterialByName(MaterialNames.LITHIUM), myTabs.itemsTab);
			createPowder(Materials.getMaterialByName(MaterialNames.LITHIUM), myTabs.itemsTab);
			createSmallPowder(Materials.getMaterialByName(MaterialNames.LITHIUM), myTabs.itemsTab);
		}

		if (Options.materialEnabled(MaterialNames.NITER)) {
			createPowder(Materials.getMaterialByName(MaterialNames.NITER), myTabs.itemsTab);
			createSmallPowder(Materials.getMaterialByName(MaterialNames.NITER), myTabs.itemsTab);
		}

		if (Options.materialEnabled(MaterialNames.PHOSPHORUS)) {
			createPowder(Materials.getMaterialByName(MaterialNames.PHOSPHORUS), myTabs.itemsTab);
			createSmallPowder(Materials.getMaterialByName(MaterialNames.PHOSPHORUS), myTabs.itemsTab);
		}

		if (Options.materialEnabled(MaterialNames.POTASH)) {
			createPowder(Materials.getMaterialByName(MaterialNames.POTASH), myTabs.itemsTab);
			createSmallPowder(Materials.getMaterialByName(MaterialNames.POTASH), myTabs.itemsTab);
		}

		if (Options.materialEnabled(MaterialNames.SALT)) {
			createPowder(Materials.getMaterialByName(MaterialNames.SALT), myTabs.itemsTab);
			createSmallPowder(Materials.getMaterialByName(MaterialNames.SALT), myTabs.itemsTab);
		}

		if (Options.materialEnabled(MaterialNames.SALTPETER)) {
			createPowder(Materials.getMaterialByName(MaterialNames.SALTPETER), myTabs.itemsTab);
			createSmallPowder(Materials.getMaterialByName(MaterialNames.SALTPETER), myTabs.itemsTab);
		}

		if (Options.materialEnabled(MaterialNames.SILICON)) {
			createBlend(Materials.getMaterialByName(MaterialNames.SILICON), myTabs.itemsTab);
			createIngot(Materials.getMaterialByName(MaterialNames.SILICON), myTabs.itemsTab);
			createNugget(Materials.getMaterialByName(MaterialNames.SILICON), myTabs.itemsTab);
			createPowder(Materials.getMaterialByName(MaterialNames.SILICON), myTabs.itemsTab);
			createSmallBlend(Materials.getMaterialByName(MaterialNames.SILICON), myTabs.itemsTab);
			createSmallPowder(Materials.getMaterialByName(MaterialNames.SILICON), myTabs.itemsTab);
		}

		if (Options.materialEnabled(MaterialNames.SULFUR)) {
			createPowder(Materials.getMaterialByName(MaterialNames.SULFUR), myTabs.itemsTab);
			createSmallPowder(Materials.getMaterialByName(MaterialNames.SULFUR), myTabs.itemsTab);
		}

		// addToMetList() // May not be needed, check

		initDone = true;
	}
}
