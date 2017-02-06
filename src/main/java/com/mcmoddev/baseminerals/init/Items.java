package com.mcmoddev.baseminerals.init;

import com.mcmoddev.baseminerals.util.Config.Options;

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

		if (Options.enableCharcoal) {
			createNugget(Materials.charcoal); // Special
			createPowder(Materials.charcoal);
			createSmallPowder(Materials.charcoal);
		}

		if (Options.enableLithium) {
			createIngot(Materials.lithium);
			createNugget(Materials.lithium);
			createPowder(Materials.lithium);
			createSmallPowder(Materials.lithium);
		}

		if (Options.enableNiter) {
			createPowder(Materials.niter);
			createSmallPowder(Materials.niter);
		}

		if (Options.enablePhosphorus) {
			createPowder(Materials.phosphorus);
			createSmallPowder(Materials.phosphorus);
		}

		if (Options.enablePotash) {
			createPowder(Materials.potash);
			createSmallPowder(Materials.potash);
		}

		if (Options.enableSalt) {
			createPowder(Materials.salt);
			createSmallPowder(Materials.salt);
		}

		if (Options.enableSaltpeter) {
			createPowder(Materials.saltpeter);
			createSmallPowder(Materials.saltpeter);
		}

		if (Options.enableSilicon) {
			createBlend(Materials.silicon);
			createIngot(Materials.silicon);
			createNugget(Materials.silicon);
			createPowder(Materials.silicon);
			createSmallBlend(Materials.silicon);
			createSmallPowder(Materials.silicon);
		}

		if (Options.enableSulfur) {
			createPowder(Materials.sulfur);
			createSmallPowder(Materials.sulfur);
		}

//		addToMetList() // May not be needed, check

		initDone = true;
	}
}
