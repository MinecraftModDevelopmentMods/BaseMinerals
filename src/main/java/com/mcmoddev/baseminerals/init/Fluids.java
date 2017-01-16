package com.mcmoddev.baseminerals.init;

import com.mcmoddev.baseminerals.util.Config.Options;

import net.minecraftforge.fluids.*;

/**
 * This class initializes all fluids in Base Minerals.
 *
 * @author DrCyano
 *
 */
public class Fluids extends cyano.basemetals.init.Fluids {

	public static Fluid fluidLithium = null;
	public static BlockFluidBase fluidBlockLithium = null;

	public static Fluid fluidSilicon = null;
	public static BlockFluidBase fluidBlockSilicon = null;

	private static boolean initDone = false;

	/**
	 *
	 */
	public static void init() {
		if (initDone) {
			return;
		}

		String materialName;

		if (Options.ENABLE_LITHIUM) {
			materialName = "lithium";

			fluidLithium = addFluid(materialName, 2000, 10000, 330, 10);
			fluidBlockLithium = addFluidBlock(materialName);
		}
		if (Options.ENABLE_SILICON) {
			materialName = "silicon";

			fluidSilicon = addFluid(materialName, 2000, 10000, 330, 10);
			fluidBlockSilicon = addFluidBlock(materialName);
		}
		initDone = true;
	}
}
