package com.mcmoddev.baseminerals.init;

import com.mcmoddev.basemetals.material.MetalMaterial;
import com.mcmoddev.baseminerals.util.Config.Options;

/**
 * This class initializes all fluids in Base Minerals.
 *
 * @author Jasmine Iwanek
 *
 */
public class Fluids extends com.mcmoddev.lib.init.Fluids {

	private static boolean initDone = false;

	/**
	 *
	 */
	public static void init() {
		if (initDone) {
			return;
		}

		MetalMaterial material;

		// fluids and fluid blocks
		if (Options.ENABLE_LITHIUM) {
			material = Materials.lithium;

			addFluid(material, 2000, 10000, 330, 10);
			addFluidBlock(material);
		}
		if (Options.ENABLE_SILICON) {
			material = Materials.silicon;

			addFluid(material, 2000, 10000, 330, 10);
			addFluidBlock(material);
		}

		initDone = true;
	}
}
