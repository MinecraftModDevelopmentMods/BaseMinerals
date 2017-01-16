package com.mcmoddev.baseminerals.init;

import com.mcmoddev.baseminerals.util.Config.Options;

import cyano.basemetals.material.MetalMaterial;

/**
 * This class initializes all blocks in Base Minerals.
 *
 * @author DrCyano
 *
 */
public class Blocks extends cyano.basemetals.init.Blocks {

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

		MetalMaterial material;

		if (Options.ENABLE_CHARCOAL) {
			material = Materials.charcoal;

			createBlock(material);
			//createOre(material);
		}

		if (Options.ENABLE_LITHIUM) {
			material = Materials.lithium;

			createBlock(material);
			createOre(material);
		}

		if (Options.ENABLE_NITER) {
			material = Materials.niter;

			createBlock(material);
			createOre(material);
		}

		if (Options.ENABLE_PHOSPHORUS) {
			material = Materials.phosphorus;

			createBlock(material);
			createOre(material);
		}

		if (Options.ENABLE_POTASH) {
			material = Materials.potash;

			createBlock(material);
			createOre(material);
		}

		if (Options.ENABLE_SALT) {
			material = Materials.salt;

			createBlock(material);
			createOre(material);
		}

		if (Options.ENABLE_SALTPETER) {
			material = Materials.saltpeter;

			createBlock(material);
			createOre(material);
		}

		if (Options.ENABLE_SILICON) {
			material = Materials.silicon;

			createBlock(material);
			//createOre(material);
		}

		if (Options.ENABLE_SULFUR) {
			material = Materials.sulfur;

			createBlock(material);
			createOre(material);
		}

		initDone = true;
	}
}
