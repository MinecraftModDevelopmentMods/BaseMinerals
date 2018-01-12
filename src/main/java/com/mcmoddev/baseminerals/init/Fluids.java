package com.mcmoddev.baseminerals.init;

import java.util.Arrays;
import java.util.List;

import com.mcmoddev.baseminerals.data.MaterialNames;
import com.mcmoddev.lib.data.SharedStrings;
import com.mcmoddev.lib.util.ConfigBase.Options;

/**
 * This class initializes all fluids in Base Minerals.
 *
 * @author Jasmine Iwanek
 *
 */
public class Fluids extends com.mcmoddev.lib.init.Fluids {

	private static boolean initDone = false;

	private Fluids() {
		throw new IllegalAccessError(SharedStrings.NOT_INSTANTIABLE);
	}

	/**
	 *
	 */
	public static void init() {
		if (initDone) {
			return;
		}

		final List<String> materials = Arrays.asList(MaterialNames.LITHIUM, MaterialNames.SILICON);

		materials.stream().filter(Materials::hasMaterial)
				.filter(materialName -> !Materials.getMaterialByName(materialName).equals(Materials.emptyMaterial))
				.filter(Options::isFluidEnabled).forEach(materialName -> {
					addFluid(materialName, 2000, 10000, 330, 10);
					addFluidBlock(materialName);
				});

		initDone = true;
	}
}
