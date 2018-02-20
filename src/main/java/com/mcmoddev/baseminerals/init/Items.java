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
			create(Names.NUGGET, charcoal); // Special
			create(Names.POWDER, charcoal);
			create(Names.SMALLPOWDER, charcoal);
			FuelRegistry.addFuel(Oredicts.NUGGET_CHARCOAL, 200);
			FuelRegistry.addFuel(Oredicts.DUST_CHARCOAL, 1600);
			FuelRegistry.addFuel(Oredicts.DUST_SMALL_CHARCOAL, 200);
			FuelRegistry.addFuel(Oredicts.DUST_TINY_CHARCOAL, 200);
			FuelRegistry.addFuel(Oredicts.BLOCK_CHARCOAL, 16000);
		}

		if (Materials.hasMaterial(MaterialNames.LITHIUM)) {
			final MMDMaterial lithium = Materials.getMaterialByName(MaterialNames.LITHIUM);
			create(Names.INGOT, lithium);
			create(Names.NUGGET, lithium);
			create(Names.POWDER, lithium);
			create(Names.SMALLPOWDER, lithium);
		}

		final List<String> materials = Arrays.asList(MaterialNames.NITER, MaterialNames.PHOSPHORUS,
				MaterialNames.POTASH, MaterialNames.SALT, MaterialNames.SALTPETER,
				MaterialNames.SULFUR);

		materials.stream().filter(Materials::hasMaterial).forEach(materialName -> {
			final MMDMaterial material = Materials.getMaterialByName(materialName);

			create(Names.POWDER, material);
			create(Names.SMALLPOWDER, material);
		});

		if (Materials.hasMaterial(MaterialNames.SILICON)) {
			final MMDMaterial silicon = Materials.getMaterialByName(MaterialNames.SILICON);
			create(Names.BLEND, silicon);
			create(Names.INGOT, silicon);
			create(Names.NUGGET, silicon);
			create(Names.POWDER, silicon);
			create(Names.SMALLBLEND, silicon);
			create(Names.SMALLPOWDER, silicon);
		}
	}
}
