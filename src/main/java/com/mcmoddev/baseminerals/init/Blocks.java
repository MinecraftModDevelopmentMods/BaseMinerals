package com.mcmoddev.baseminerals.init;

import com.mcmoddev.baseminerals.util.Config.Options;

/**
 * This class initializes all blocks in Base Minerals.
 *
 * @author Jasmine Iwanek
 *
 */
public class Blocks extends com.mcmoddev.lib.init.Blocks {

	private static boolean initDone = false;

	/**
	 *
	 */
	public static void init() {
		if (initDone) {
			return;
		}

		Materials.init();
		ItemGroups.init();

		if (Options.enableCharcoal) {
			createBlock(Materials.charcoal);
		}

		if (Options.enableLithium) {
			createBlock(Materials.lithium);
			createOre(Materials.lithium);
		}

		if (Options.enableNiter) {
			createBlock(Materials.niter);
			createOre(Materials.niter);
		}

		if (Options.enablePhosphorus) {
			createBlock(Materials.phosphorus);
			createOre(Materials.phosphorus);
		}

		if (Options.enablePotash) {
			createBlock(Materials.potash);
			createOre(Materials.potash);
		}

		if (Options.enableSalt) {
			createBlock(Materials.salt);
			createOre(Materials.salt);
		}

		if (Options.enableSaltpeter) {
			createBlock(Materials.saltpeter);
			createOre(Materials.saltpeter);
		}

		if (Options.enableSilicon) {
			createBlock(Materials.silicon);
		}

		if (Options.enableSulfur) {
			createBlock(Materials.sulfur);
			createOre(Materials.sulfur);
		}

		initDone = true;
	}
}
