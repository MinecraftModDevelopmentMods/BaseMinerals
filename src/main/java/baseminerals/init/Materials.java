package baseminerals.init;

import cyano.basemetals.material.MetalMaterial;

/**
 * This class initializes all of the metal materials in Base Minerals. It also
 * contains utility methods for looking up materials by name and finding the 
 * tool and armor material equivalents for a given metal.
 * @author DrCyano
 *
 */
public abstract class Materials extends cyano.basemetals.init.Materials {

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
		if(initDone) return;

		// Mod Metals
		charcoal = addMaterial("charcoal", 8, 8, 4.5);
		lithium = addMaterial("lithium", 8, 8, 4.5);
		niter = addMaterial("niter", 8, 8, 4.5);
		phosphorus = addMaterial("phosphorus", 8, 8, 4.5);
		potash = addMaterial("potash", 8, 8, 4.5);
		salt = addMaterial("salt", 8, 8, 4.5);
		saltpeter = addMaterial("saltpeter", 8, 8, 4.5);
		silicon = addMaterial("silicon", 8, 8, 4.5);
		sulfur = addMaterial("sulfur", 8, 8, 4.5);

		initDone = true;
	}

	private static MetalMaterial addMaterial(String name, double hardness, double strength, double magic) {
		MetalMaterial m = new MetalMaterial(name, (float)hardness, (float)strength, (float)magic);
		registerMaterial(name, m);
		return m;
	}

	private static MetalMaterial addRareMaterial(String name, double hardness, double strength, double magic) {
		MetalMaterial m = new MetalMaterial(name, (float)hardness, (float)strength, (float)magic);
		registerMaterial(name, m);
		return m;
	}
}
