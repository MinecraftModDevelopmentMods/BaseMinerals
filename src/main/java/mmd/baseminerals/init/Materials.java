package mmd.baseminerals.init;

import cyano.basemetals.material.MetalMaterial;
import mmd.baseminerals.util.Config.Options;

/**
 * This class initializes all of the materials in Base Minerals.
 *
 * @author DrCyano
 *
 */
public class Materials extends cyano.basemetals.init.Materials {

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
			charcoal = createMaterial("charcoal", 8, 8, 4.5, 0xFF000000);
		}
		if (Options.ENABLE_LITHIUM) {
			lithium = createMaterial("lithium", 8, 8, 4.5, 0xFFC5C8C1);
		}
		if (Options.ENABLE_NITER) {
			niter = createMaterial("niter", 8, 8, 4.5, 0xFF000000);
		}
		if (Options.ENABLE_PHOSPHORUS) {
			phosphorus = createMaterial("phosphorus", 8, 8, 4.5, 0xFF000000);
		}
		if (Options.ENABLE_POTASH) {
			potash = createMaterial("potash", 8, 8, 4.5, 0xFF000000);
		}
		if (Options.ENABLE_SALT) {
			salt = createMaterial("salt", 8, 8, 4.5, 0xFF000000);
		}
		if (Options.ENABLE_SALTPETER) {
			saltpeter = createMaterial("saltpeter", 8, 8, 4.5, 0xFF000000);
		}
		if (Options.ENABLE_SILICON) {
			silicon = createMaterial("silicon", 8, 8, 4.5, 0xFFC5C8C1);
		}
		if (Options.ENABLE_SULFUR) {
			sulfur = createMaterial("sulfur", 8, 8, 4.5, 0xFF000000);
		}

		initDone = true;
	}
}
