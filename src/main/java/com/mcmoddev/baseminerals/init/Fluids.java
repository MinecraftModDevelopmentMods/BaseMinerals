package com.mcmoddev.baseminerals.init;

import com.mcmoddev.baseminerals.data.MaterialNames;
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
			addFluid(Materials.getMaterialByName(MaterialNames.LITHIUM), 2000, 10000, 330, 10);
			addFluidBlock(Materials.getMaterialByName(MaterialNames.LITHIUM));
		}

		if (Options.enableSilicon) {
			addFluid(Materials.getMaterialByName(MaterialNames.SILICON), 2000, 10000, 330, 10);
			addFluidBlock(Materials.getMaterialByName(MaterialNames.SILICON));
		}

		initDone = true;
	}
}
