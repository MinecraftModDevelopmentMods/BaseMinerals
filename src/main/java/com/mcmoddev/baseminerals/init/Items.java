package com.mcmoddev.baseminerals.init;

import java.util.Arrays;
import java.util.List;

import com.mcmoddev.baseminerals.data.MaterialNames;
import com.mcmoddev.lib.data.Names;
import com.mcmoddev.lib.data.SharedStrings;
import com.mcmoddev.lib.fuels.FuelRegistry;
import com.mcmoddev.lib.init.Materials;
import com.mcmoddev.lib.material.MMDMaterial;
import com.mcmoddev.lib.util.Oredicts;

/**
 * This class initializes all items in Base Minerals.
 *
 * @author Jasmine Iwanek
 *
 */
public class Items extends com.mcmoddev.lib.init.Items {

	protected Items() {
		throw new IllegalAccessError(SharedStrings.NOT_INSTANTIABLE);
	}

	/**
	 *
	 */
	public static void init() {

		if (Materials.hasMaterial(MaterialNames.CHARCOAL)) {
			final MMDMaterial charcoal = Materials.getMaterialByName(MaterialNames.CHARCOAL);
			create(Names.NUGGET, charcoal, ItemGroups.myTabs.itemsTab); // Special
			create(Names.POWDER, charcoal, ItemGroups.myTabs.itemsTab);
			create(Names.SMALLPOWDER, charcoal, ItemGroups.myTabs.itemsTab);
			FuelRegistry.addFuel(Oredicts.NUGGET_CHARCOAL, 200);
			FuelRegistry.addFuel(Oredicts.DUST_CHARCOAL, 1600);
			FuelRegistry.addFuel(Oredicts.DUST_SMALL_CHARCOAL, 200);
			FuelRegistry.addFuel(Oredicts.DUST_TINY_CHARCOAL, 200);
			FuelRegistry.addFuel(Oredicts.BLOCK_CHARCOAL, 16000);
		}

		if (Materials.hasMaterial(MaterialNames.LITHIUM)) {
			final MMDMaterial lithium = Materials.getMaterialByName(MaterialNames.LITHIUM);
			create(Names.INGOT, lithium, ItemGroups.myTabs.itemsTab);
			create(Names.NUGGET, lithium, ItemGroups.myTabs.itemsTab);
			create(Names.POWDER, lithium, ItemGroups.myTabs.itemsTab);
			create(Names.SMALLPOWDER, lithium, ItemGroups.myTabs.itemsTab);
		}

		final List<String> materials = Arrays.asList(MaterialNames.NITER, MaterialNames.PHOSPHORUS,
				MaterialNames.POTASH, MaterialNames.SALT, MaterialNames.SALTPETER,
				MaterialNames.SULFUR);

		materials.stream().filter(Materials::hasMaterial).forEach(materialName -> {
			final MMDMaterial material = Materials.getMaterialByName(materialName);

			create(Names.POWDER, material, ItemGroups.myTabs.itemsTab);
			create(Names.SMALLPOWDER, material, ItemGroups.myTabs.itemsTab);
		});

		if (Materials.hasMaterial(MaterialNames.SILICON)) {
			final MMDMaterial silicon = Materials.getMaterialByName(MaterialNames.SILICON);
			create(Names.BLEND, silicon, ItemGroups.myTabs.itemsTab);
			create(Names.INGOT, silicon, ItemGroups.myTabs.itemsTab);
			create(Names.NUGGET, silicon, ItemGroups.myTabs.itemsTab);
			create(Names.POWDER, silicon, ItemGroups.myTabs.itemsTab);
			create(Names.SMALLBLEND, silicon, ItemGroups.myTabs.itemsTab);
			create(Names.SMALLPOWDER, silicon, ItemGroups.myTabs.itemsTab);
		}
	}
}
