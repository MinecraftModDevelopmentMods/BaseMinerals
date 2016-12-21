package mmd.baseminerals.init;

import mmd.baseminerals.util.Config.Options;
import net.minecraft.block.*;

/**
 * This class initializes all blocks in Base Minerals.
 *
 * @author DrCyano
 *
 */
public class Blocks extends cyano.basemetals.init.Blocks {

	public static Block charcoal_block;
//	public static Block charcoal_ore;

	public static Block lithium_block;
	public static Block lithium_ore;

	public static Block niter_block;
	public static Block niter_ore;

	public static Block phosphorus_block;
	public static Block phosphorus_ore;

	public static Block potash_block;
	public static Block potash_ore;

	public static Block salt_block;
	public static Block salt_ore;

	public static Block saltpeter_block;
	public static Block saltpeter_ore;

	public static Block silicon_block;
//	public static Block silicon_ore;

	public static Block sulfur_block;
	public static Block sulfur_ore;

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

		String materialName;

		if (Options.ENABLE_CHARCOAL) {
			materialName = "charcoal";

			charcoal_block = createBlock(materialName);
			//charcoal_ore = createOre(materialName);
		}

		if (Options.ENABLE_LITHIUM) {
			materialName = "lithium";

			lithium_block = createBlock(materialName);
			lithium_ore = createOre(materialName);
		}

		if (Options.ENABLE_NITER) {
			materialName = "niter";

			niter_block = createBlock(materialName);
			niter_ore = createOre(materialName);
		}

		if (Options.ENABLE_PHOSPHORUS) {
			materialName = "phosphorus";

			phosphorus_block = createBlock(materialName);
			phosphorus_ore = createOre(materialName);
		}

		if (Options.ENABLE_POTASH) {
			materialName = "potash";

			potash_block = createBlock(materialName);
			potash_ore = createOre(materialName);
		}

		if (Options.ENABLE_SALT) {
			materialName = "salt";

			salt_block = createBlock(materialName);
			salt_ore = createOre(materialName);
		}

		if (Options.ENABLE_SALTPETER) {
			materialName = "saltpeter";

			saltpeter_block = createBlock(materialName);
			saltpeter_ore = createOre(materialName);
		}

		if (Options.ENABLE_SILICON) {
			materialName = "silicon";

			silicon_block = createBlock(materialName);
			//silicon_ore = createOre(materialName);
		}

		if (Options.ENABLE_SULFUR) {
			materialName = "sulfur";

			sulfur_block = createBlock(materialName);
			sulfur_ore = createOre(materialName);
		}

		initDone = true;
	}
}
