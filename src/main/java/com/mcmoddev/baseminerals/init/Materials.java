package com.mcmoddev.baseminerals.init;

import com.mcmoddev.lib.util.ConfigBase.Options;

import com.mcmoddev.lib.material.MMDMaterial.MaterialType;

import com.mcmoddev.baseminerals.data.MaterialNames;

/**
 * This class initializes all of the materials in Base Minerals.
 *
 * @author Jasmine Iwanek
 *
 */
public class Materials extends com.mcmoddev.lib.init.Materials {

	private static boolean initDone = false;

	protected Materials() {
		throw new IllegalAccessError("Not a instantiable class");
	}

	/**
	 *
	 */
	public static void init() {
		if (initDone) {
			return;
		}

		if (Options.isMaterialEnabled("Lithium")) {
			createMaterial(MaterialNames.LITHIUM, MaterialType.METAL, 8, 8, 4.5, 0xFFC5C8C1);
		}

		if (Options.isMaterialEnabled("Niter")) {
			createMaterial(MaterialNames.NITER, MaterialType.MINERAL, 5, 2, 0, 0xFF000000);
		}

		if (Options.isMaterialEnabled("Phosphorus")) {
			createMaterial(MaterialNames.PHOSPHORUS, MaterialType.MINERAL, 5, 2, 0, 0xFF000000);
		}

		if (Options.isMaterialEnabled("Potash")) {
			createMaterial(MaterialNames.POTASH, MaterialType.MINERAL, 5, 2, 0, 0xFF000000);
		}

		if (Options.isMaterialEnabled("Salt")) {
			createMaterial(MaterialNames.SALT, MaterialType.MINERAL, 5, 2, 0, 0xFF000000);
		}

		if (Options.isMaterialEnabled("Saltpeter")) {
			createMaterial(MaterialNames.SALTPETER, MaterialType.MINERAL, 5, 2, 0, 0xFF000000);
		}

		if (Options.isMaterialEnabled("Silicon")) {
			createMaterial(MaterialNames.SILICON, MaterialType.METAL, 8, 8, 4.5, 0xFFC5C8C1);
		}

		if (Options.isMaterialEnabled("Sulfur")) {
			createMaterial(MaterialNames.SULFUR, MaterialType.MINERAL, 5, 2, 0, 0xFF000000);
		}

		initDone = true;
	}
}
