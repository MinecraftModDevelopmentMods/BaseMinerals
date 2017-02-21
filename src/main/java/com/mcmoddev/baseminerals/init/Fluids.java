package com.mcmoddev.baseminerals.init;

import com.mcmoddev.baseminerals.util.Config.Options;

/**
 * This class initializes all fluids in Base Minerals.
 *
 * @author Jasmine Iwanek
 *
 */
public class Fluids extends com.mcmoddev.lib.init.Fluids {

	private static boolean initDone = false;

	private Fluids() {
		throw new IllegalAccessError("Not a instantiable class");
	}

	/**
	 *
	 */
	public static void init() {
		if (initDone) {
			return;
		}

		if (Options.enableLithium) {
			addFluid(Materials.lithium, 2000, 10000, 330, 10);
			addFluidBlock(Materials.lithium);
		}

		if (Options.enableSilicon) {
			addFluid(Materials.silicon, 2000, 10000, 330, 10);
			addFluidBlock(Materials.silicon);
		}

		initDone = true;
	}
}
