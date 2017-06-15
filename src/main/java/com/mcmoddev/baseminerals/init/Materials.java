package com.mcmoddev.baseminerals.init;

import com.mcmoddev.baseminerals.util.Config.Options;

import com.mcmoddev.lib.material.MMDMaterial;
import com.mcmoddev.lib.material.MMDMaterial.MaterialType;

import com.mcmoddev.baseminerals.data.MaterialNames;

/**
 * This class initializes all of the materials in Base Minerals.
 *
 * @author Jasmine Iwanek
 *
 */
public class Materials extends com.mcmoddev.lib.init.Materials {

	/** Lithium */
	public static MMDMaterial lithium;
	/** Niter */
	public static MMDMaterial niter;
	/** Phosphorus */
	public static MMDMaterial phosphorus;
	/** Potash */
	public static MMDMaterial potash;
	/** Salt */
	public static MMDMaterial salt;
	/** Saltpeter */
	public static MMDMaterial saltpeter;
	/** Silicon */
	public static MMDMaterial silicon;
	/** Sulfur */
	public static MMDMaterial sulfur;

	// vanilla imports
	/** Charcoal */
	public static MMDMaterial vanilla_charcoal;
	/** Coal */
	public static MMDMaterial vanilla_coal;

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

		if (Options.enableCharcoal) {
			vanilla_charcoal = createMaterial(MaterialNames.CHARCOAL, MaterialType.MINERAL, 5, 2, 0, 0xFF000000);
		}

		if (Options.enableLithium) {
			lithium = createMaterial(MaterialNames.LITHIUM, MaterialType.METAL, 8, 8, 4.5, 0xFFC5C8C1);
		}

		if (Options.enableNiter) {
			niter = createMaterial(MaterialNames.NITER, MaterialType.MINERAL, 5, 2, 0, 0xFF000000);
		}

		if (Options.enablePhosphorus) {
			phosphorus = createMaterial(MaterialNames.PHOSPHORUS, MaterialType.MINERAL, 5, 2, 0, 0xFF000000);
		}

		if (Options.enablePotash) {
			potash = createMaterial(MaterialNames.POTASH, MaterialType.MINERAL, 5, 2, 0, 0xFF000000);
		}

		if (Options.enableSalt) {
			salt = createMaterial(MaterialNames.SALT, MaterialType.MINERAL, 5, 2, 0, 0xFF000000);
		}

		if (Options.enableSaltpeter) {
			saltpeter = createMaterial(MaterialNames.SALTPETER, MaterialType.MINERAL, 5, 2, 0, 0xFF000000);
		}

		if (Options.enableSilicon) {
			silicon = createMaterial(MaterialNames.SILICON, MaterialType.METAL, 8, 8, 4.5, 0xFFC5C8C1);
		}

		if (Options.enableSulfur) {
			sulfur = createMaterial(MaterialNames.SULFUR, MaterialType.MINERAL, 5, 2, 0, 0xFF000000);
		}

		initDone = true;
	}
}
