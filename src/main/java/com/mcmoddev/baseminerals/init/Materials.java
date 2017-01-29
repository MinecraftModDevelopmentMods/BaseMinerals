package com.mcmoddev.baseminerals.init;

import com.mcmoddev.baseminerals.util.Config.Options;

import com.mcmoddev.basemetals.material.MetalMaterial;

/**
 * This class initializes all of the materials in Base Minerals.
 *
 * @author DrCyano
 *
 */
public class Materials extends com.mcmoddev.lib.init.Materials {

	public static MetalMaterial charcoal;
	public static MetalMaterial lithium;
	public static MetalMaterial niter;
	public static MetalMaterial phosphorus;
	public static MetalMaterial potash;
	public static MetalMaterial salt;
	public static MetalMaterial saltpeter;
	public static MetalMaterial silicon;
	public static MetalMaterial sulfur;

	private static boolean initDone = false;

	/**
	 *
	 */
	public static void init() {
		if (initDone) {
			return;
		}

		// Mod Materials
		if (Options.ENABLE_CHARCOAL) {
			charcoal = createMaterial("charcoal", 5, 2, 0, 0xFF000000);
		}
		if (Options.ENABLE_LITHIUM) {
			lithium = createMaterial("lithium", 8, 8, 4.5, 0xFFC5C8C1);
		}
		if (Options.ENABLE_NITER) {
			niter = createMaterial("niter", 5, 2, 0, 0xFF000000);
		}
		if (Options.ENABLE_PHOSPHORUS) {
			phosphorus = createMaterial("phosphorus", 5, 2, 0, 0xFF000000);
		}
		if (Options.ENABLE_POTASH) {
			potash = createMaterial("potash", 5, 2, 0, 0xFF000000);
		}
		if (Options.ENABLE_SALT) {
			salt = createMaterial("salt", 5, 2, 0, 0xFF000000);
		}
		if (Options.ENABLE_SALTPETER) {
			saltpeter = createMaterial("saltpeter", 5, 2, 0, 0xFF000000);
		}
		if (Options.ENABLE_SILICON) {
			silicon = createMaterial("silicon", 8, 8, 4.5, 0xFFC5C8C1);
		}
		if (Options.ENABLE_SULFUR) {
			sulfur = createMaterial("sulfur", 5, 2, 0, 0xFF000000);
		}

		initDone = true;
	}
}
