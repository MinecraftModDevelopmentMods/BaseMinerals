package com.mcmoddev.baseminerals.init;

import com.mcmoddev.baseminerals.util.Config.Options;

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

		if (Options.materialEnabled(MaterialNames.LITHIUM)) {
			createMaterial(MaterialNames.LITHIUM, MaterialType.METAL, 8, 8, 4.5, 0xFFC5C8C1);
		}

		if (Options.materialEnabled(MaterialNames.NITER)) {
			createMaterial(MaterialNames.NITER, MaterialType.MINERAL, 5, 2, 0, 0xFF000000);
		}

		if (Options.materialEnabled(MaterialNames.PHOSPHORUS)) {
			createMaterial(MaterialNames.PHOSPHORUS, MaterialType.MINERAL, 5, 2, 0, 0xFF000000);
		}

		if (Options.materialEnabled(MaterialNames.POTASH)) {
			createMaterial(MaterialNames.POTASH, MaterialType.MINERAL, 5, 2, 0, 0xFF000000);
		}

		if (Options.materialEnabled(MaterialNames.SALT)) {
			createMaterial(MaterialNames.SALT, MaterialType.MINERAL, 5, 2, 0, 0xFF000000);
		}

		if (Options.materialEnabled(MaterialNames.SALTPETER)) {
			createMaterial(MaterialNames.SALTPETER, MaterialType.MINERAL, 5, 2, 0, 0xFF000000);
		}

		if (Options.materialEnabled(MaterialNames.SILICON)) {
			createMaterial(MaterialNames.SILICON, MaterialType.METAL, 8, 8, 4.5, 0xFFC5C8C1);
		}

		if (Options.materialEnabled(MaterialNames.SULFUR)) {
			createMaterial(MaterialNames.SULFUR, MaterialType.MINERAL, 5, 2, 0, 0xFF000000);
		}

		initDone = true;
	}
}
