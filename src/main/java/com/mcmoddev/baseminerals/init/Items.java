package com.mcmoddev.baseminerals.init;

import com.mcmoddev.lib.util.ConfigBase.Options;
import com.mcmoddev.lib.data.Names;
import com.mcmoddev.lib.init.Materials;
import com.mcmoddev.lib.item.ItemMMDNugget;
import com.mcmoddev.lib.item.ItemMMDPowder;
import com.mcmoddev.lib.item.ItemMMDSmallPowder;
import com.mcmoddev.lib.material.MMDMaterial;
import com.mcmoddev.lib.util.Oredicts;
import com.mcmoddev.baseminerals.data.MaterialNames;

/**
 * This class initializes all items in Base Minerals.
 *
 * @author Jasmine Iwanek
 *
 */
public class Items extends com.mcmoddev.lib.init.Items {

	private static boolean initDone = false;

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

		if (Options.isMaterialEnabled(MaterialNames.CHARCOAL)) {
			final MMDMaterial material = Materials.getMaterialByName(MaterialNames.CHARCOAL);
			create(Names.NUGGET, material, ItemGroups.myTabs.itemsTab); // Special
			create(Names.POWDER, material, ItemGroups.myTabs.itemsTab);
			create(Names.SMALLPOWDER, material, ItemGroups.myTabs.itemsTab);
			((ItemMMDNugget)Materials.getMaterialByName(MaterialNames.CHARCOAL).getItem(Names.NUGGET)).setBurnTime(200);
			((ItemMMDPowder)Materials.getMaterialByName(MaterialNames.CHARCOAL).getItem(Names.POWDER)).setBurnTime(1600);
			((ItemMMDSmallPowder)Materials.getMaterialByName(MaterialNames.CHARCOAL).getItem(Names.SMALLPOWDER)).setBurnTime(200);
		}

		if (Options.isMaterialEnabled(MaterialNames.LITHIUM)) {
			final MMDMaterial material = Materials.getMaterialByName(MaterialNames.LITHIUM);
			create(Names.INGOT, material, ItemGroups.myTabs.itemsTab);
			create(Names.NUGGET, material, ItemGroups.myTabs.itemsTab);
			create(Names.POWDER, material, ItemGroups.myTabs.itemsTab);
			create(Names.SMALLPOWDER, material, ItemGroups.myTabs.itemsTab);
		}

		if (Options.isMaterialEnabled(MaterialNames.NITER)) {
			final MMDMaterial material = Materials.getMaterialByName(MaterialNames.NITER);
			create(Names.POWDER, material, ItemGroups.myTabs.itemsTab);
			create(Names.SMALLPOWDER, material, ItemGroups.myTabs.itemsTab);
		}

		if (Options.isMaterialEnabled(MaterialNames.PHOSPHORUS)) {
			final MMDMaterial material = Materials.getMaterialByName(MaterialNames.PHOSPHORUS);
			create(Names.POWDER, material, ItemGroups.myTabs.itemsTab);
			create(Names.SMALLPOWDER, material, ItemGroups.myTabs.itemsTab);
		}

		if (Options.isMaterialEnabled(MaterialNames.POTASH)) {
			final MMDMaterial material = Materials.getMaterialByName(MaterialNames.POTASH);
			create(Names.POWDER, material, ItemGroups.myTabs.itemsTab);
			create(Names.SMALLPOWDER, material, ItemGroups.myTabs.itemsTab);
		}

		if (Options.isMaterialEnabled(MaterialNames.SALT)) {
			final MMDMaterial material = Materials.getMaterialByName(MaterialNames.SALT);
			create(Names.POWDER, material, ItemGroups.myTabs.itemsTab);
			create(Names.SMALLPOWDER, material, ItemGroups.myTabs.itemsTab);
		}

		if (Options.isMaterialEnabled(MaterialNames.SALTPETER)) {
			final MMDMaterial material = Materials.getMaterialByName(MaterialNames.SALTPETER);
			create(Names.POWDER, material, ItemGroups.myTabs.itemsTab);
			create(Names.SMALLPOWDER, material, ItemGroups.myTabs.itemsTab);
		}

		if (Options.isMaterialEnabled(MaterialNames.SILICON)) {
			final MMDMaterial material = Materials.getMaterialByName(MaterialNames.SILICON);
			create(Names.BLEND, material, ItemGroups.myTabs.itemsTab);
			create(Names.INGOT, material, ItemGroups.myTabs.itemsTab);
			create(Names.NUGGET, material, ItemGroups.myTabs.itemsTab);
			create(Names.POWDER, material, ItemGroups.myTabs.itemsTab);
			create(Names.SMALLBLEND, material, ItemGroups.myTabs.itemsTab);
			create(Names.SMALLPOWDER, material, ItemGroups.myTabs.itemsTab);
		}

		if (Options.isMaterialEnabled(MaterialNames.SULFUR)) {
			final MMDMaterial material = Materials.getMaterialByName(MaterialNames.SULFUR);
			create(Names.POWDER, material, ItemGroups.myTabs.itemsTab);
			create(Names.SMALLPOWDER, material, ItemGroups.myTabs.itemsTab);
		}

		// addToMetList() // TODO: May not be needed, check

		initDone = true;
	}
}
