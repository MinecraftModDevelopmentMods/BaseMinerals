package com.mcmoddev.baseminerals.init;

import com.mcmoddev.lib.util.ConfigBase.Options;
import com.mcmoddev.lib.data.Names;
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

		if (Options.isMaterialEnabled("Charcoal")) {
			create(Names.NUGGET, Materials.getMaterialByName(MaterialNames.CHARCOAL), myTabs.itemsTab); // Special
			create(Names.POWDER, Materials.getMaterialByName(MaterialNames.CHARCOAL), myTabs.itemsTab);
			create(Names.SMALLPOWDER, Materials.getMaterialByName(MaterialNames.CHARCOAL), myTabs.itemsTab);
			FuelRegistry.addFuel(Oredicts.NUGGET_CHARCOAL, 200);
			FuelRegistry.addFuel(Oredicts.DUST_CHARCOAL, 1600);
			FuelRegistry.addFuel(Oredicts.DUST_SMALL_CHARCOAL, 200);
			FuelRegistry.addFuel(Oredicts.DUST_TINY_CHARCOAL, 200);
			FuelRegistry.addFuel(Oredicts.BLOCK+"Charcoal", 16000);
		}

		if (Options.isMaterialEnabled("Lithium")) {
			create(Names.INGOT, Materials.getMaterialByName(MaterialNames.LITHIUM), myTabs.itemsTab);
			create(Names.NUGGET, Materials.getMaterialByName(MaterialNames.LITHIUM), myTabs.itemsTab);
			create(Names.POWDER, Materials.getMaterialByName(MaterialNames.LITHIUM), myTabs.itemsTab);
			create(Names.SMALLPOWDER, Materials.getMaterialByName(MaterialNames.LITHIUM), myTabs.itemsTab);
		}

		if (Options.isMaterialEnabled("Niter")) {
			create(Names.POWDER, Materials.getMaterialByName(MaterialNames.NITER), myTabs.itemsTab);
			create(Names.SMALLPOWDER, Materials.getMaterialByName(MaterialNames.NITER), myTabs.itemsTab);
		}

		if (Options.isMaterialEnabled("Phosphorus")) {
			create(Names.POWDER, Materials.getMaterialByName(MaterialNames.PHOSPHORUS), myTabs.itemsTab);
			create(Names.SMALLPOWDER, Materials.getMaterialByName(MaterialNames.PHOSPHORUS), myTabs.itemsTab);
		}

		if (Options.isMaterialEnabled("Potash")) {
			create(Names.POWDER, Materials.getMaterialByName(MaterialNames.POTASH), myTabs.itemsTab);
			create(Names.SMALLPOWDER, Materials.getMaterialByName(MaterialNames.POTASH), myTabs.itemsTab);
		}

		if (Options.isMaterialEnabled("Salt")) {
			create(Names.POWDER, Materials.getMaterialByName(MaterialNames.SALT), myTabs.itemsTab);
			create(Names.SMALLPOWDER, Materials.getMaterialByName(MaterialNames.SALT), myTabs.itemsTab);
		}

		if (Options.isMaterialEnabled("Saltpeter")) {
			create(Names.POWDER, Materials.getMaterialByName(MaterialNames.SALTPETER), myTabs.itemsTab);
			create(Names.SMALLPOWDER, Materials.getMaterialByName(MaterialNames.SALTPETER), myTabs.itemsTab);
		}

		if (Options.isMaterialEnabled("Silicon")) {
			create(Names.BLEND, Materials.getMaterialByName(MaterialNames.SILICON), myTabs.itemsTab);
			create(Names.INGOT, Materials.getMaterialByName(MaterialNames.SILICON), myTabs.itemsTab);
			create(Names.NUGGET, Materials.getMaterialByName(MaterialNames.SILICON), myTabs.itemsTab);
			create(Names.POWDER, Materials.getMaterialByName(MaterialNames.SILICON), myTabs.itemsTab);
			create(Names.SMALLBLEND, Materials.getMaterialByName(MaterialNames.SILICON), myTabs.itemsTab);
			create(Names.SMALLPOWDER, Materials.getMaterialByName(MaterialNames.SILICON), myTabs.itemsTab);
		}

		if (Options.isMaterialEnabled("Sulfur")) {
			create(Names.POWDER, Materials.getMaterialByName(MaterialNames.SULFUR), myTabs.itemsTab);
			create(Names.SMALLPOWDER, Materials.getMaterialByName(MaterialNames.SULFUR), myTabs.itemsTab);
		}

		// addToMetList() // May not be needed, check

		initDone = true;
	}
}
