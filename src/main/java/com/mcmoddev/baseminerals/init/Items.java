package com.mcmoddev.baseminerals.init;

import com.mcmoddev.baseminerals.util.Config.Options;

import cyano.basemetals.material.MetalMaterial;

/**
 * This class initializes all items in Base Minerals.
 *
 * @author DrCyano
 *
 */
public class Items extends cyano.basemetals.init.Items {

	private static boolean initDone = false;

	/**
	 *
	 */
	public static void init() {
		if (initDone) {
			return;
		}

		Blocks.init();

		MetalMaterial material;

		if (Options.ENABLE_CHARCOAL) {
			material = Materials.charcoal;

//			createBlend(material);
//			createIngot(material);
			createNugget(material); // Special
			createPowder(material);
//			createSmallBlend(material);
			createSmallPowder(material);
		}

		if (Options.ENABLE_LITHIUM) {
			material = Materials.lithium;

//			createBlend(material);
			createIngot(material);
			createNugget(material);
			createPowder(material);
//			createSmallBlend(material);
			createSmallPowder(material);
		}

		if (Options.ENABLE_NITER) {
			material = Materials.niter;

//			createBlend(material);
//			createIngot(material);
//			createNugget(material);
			createPowder(material);
//			createSmallBlend(material);
			createSmallPowder(material);
		}

		if (Options.ENABLE_PHOSPHORUS) {
			material = Materials.phosphorus;

//			createBlend(material);
//			createIngot(material);
//			createNugget(material);
			createPowder(material);
//			createSmallBlend(material);
			createSmallPowder(material);
		}

		if (Options.ENABLE_POTASH) {
			material = Materials.potash;

//			createBlend(material);
//			createIngot(material);
//			createNugget(material);
			createPowder(material);
//			createSmallBlend(material);
			createSmallPowder(material);
		}

		if (Options.ENABLE_SALT) {
			material = Materials.salt;

//			createBlend(material);
//			createIngot(material);
//			createNugget(material);
			createPowder(material);
//			createSmallblend(material);
			createSmallPowder(material);
		}

		if (Options.ENABLE_SALTPETER) {
			material = Materials.saltpeter;

//			createBlend(material);
//			createIngot(material);
//			createNugget(material);
			createPowder(material);
//			createSmallBlend(material);
			createSmallPowder(material);
		}

		if (Options.ENABLE_SILICON) {
			material = Materials.silicon;

			createBlend(material);
			createIngot(material);
			createNugget(material);
			createPowder(material);
			createSmallBlend(material);
			createSmallPowder(material);
		}

		if (Options.ENABLE_SULFUR) {
			material = Materials.sulfur;

//			createBlend(material);
//			createIngot(material);
//			createNugget(material);
			createPowder(material);
//			createSmallBlend(material);
			createSmallPowder(material);
		}

//		addToMetList() // May not be needed, check

		initDone = true;
	}
}
