package mmd.baseminerals.init;

import mmd.baseminerals.util.Config.Options;
import net.minecraft.item.*;

/**
 * This class initializes all items in Base Minerals.
 *
 * @author DrCyano
 *
 */
public class Items extends cyano.basemetals.init.Items {

//	public static Item charcoal_blend;
//	public static Item charcoal_ingot;
	public static Item charcoal_nugget;
	public static Item charcoal_powder;
//	public static Item charcoal_smallblend;
	public static Item charcoal_smallpowder;

//	public static Item lithium_blend;
	public static Item lithium_ingot;
	public static Item lithium_nugget;
	public static Item lithium_powder;
//	public static Item lithium_smallblend;
	public static Item lithium_smallpowder;

//	public static Item niter_blend;
//	public static Item niter_ingot;
//	public static Item niter_nugget;
	public static Item niter_powder;
//	public static Item niter_smallblend;
	public static Item niter_smallpowder;

//	public static Item phosphorus_blend;
//	public static Item phosphorus_ingot;
//	public static Item phosphorus_nugget;
	public static Item phosphorus_powder;
//	public static Item phosphorus_smallblend;
	public static Item phosphorus_smallpowder;

//	public static Item potash_blend;
//	public static Item potash_ingot;
//	public static Item potash_nugget;
	public static Item potash_powder;
//	public static Item potash_smallblend;
	public static Item potash_smallpowder;

//	public static Item salt_blend;
//	public static Item salt_ingot;
//	public static Item salt_nugget;
	public static Item salt_powder;
//	public static Item salt_smallblend;
	public static Item salt_smallpowder;

//	public static Item saltpeter_blend;
//	public static Item saltpeter_ingot;
//	public static Item saltpeter_nugget;
	public static Item saltpeter_powder;
//	public static Item saltpeter_smallblend;
	public static Item saltpeter_smallpowder;

	public static Item silicon_blend;
	public static Item silicon_ingot;
	public static Item silicon_nugget;
	public static Item silicon_powder;
	public static Item silicon_smallblend;
	public static Item silicon_smallpowder;

//	public static Item sulfur_blend;
//	public static Item sulfur_ingot;
//	public static Item sulfur_nugget;
	public static Item sulfur_powder;
//	public static Item sulfur_smallblend;
	public static Item sulfur_smallpowder;

	private static boolean initDone = false;

	/**
	 *
	 */
	public static void init() {
		if (initDone) {
			return;
		}

		Blocks.init();

		String materialName;

		if (Options.ENABLE_CHARCOAL) {
			materialName = "charcoal";

//			charcoal_blend = createBlend(materialName);
//			charcoal_ingot = createIngot(materialName);
			charcoal_nugget = createNugget(materialName); // Special
			charcoal_powder = createPowder(materialName);
//			charcoal_smallblend = createSmallBlend(materialName);
			charcoal_smallpowder = createSmallPowder(materialName);
		}

		if (Options.ENABLE_LITHIUM) {
			materialName = "lithium";

//			lithium_blend = createBlend(materialName);
			lithium_ingot = createIngot(materialName);
			lithium_nugget = createNugget(materialName);
			lithium_powder = createPowder(materialName);
//			lithium_smallblend = createSmallBlend(materialName);
			lithium_smallpowder = createSmallPowder(materialName);
		}

		if (Options.ENABLE_NITER) {
			materialName = "niter";

//			niter_blend = createBlend(materialName);
//			niter_ingot = createIngot(materialName);
//			niter_nugget = createNugget(materialName);
			niter_powder = createPowder(materialName);
//			niter_smallblend = createSmallBlend(materialName);
			niter_smallpowder = createSmallPowder(materialName);
		}

		if (Options.ENABLE_PHOSPHORUS) {
			materialName = "phosphorus";

//			phosphorus_blend = createBlend(materialName);
//			phosphorus_ingot = createIngot(materialName);
//			phosphorus_nugget = createNugget(materialName);
			phosphorus_powder = createPowder(materialName);
//			phosphorus_smallblend = createSmallBlend(materialName);
			phosphorus_smallpowder = createSmallPowder(materialName);
		}

		if (Options.ENABLE_POTASH) {
			materialName = "potash";

//			potash_blend = createBlend(materialName);
//			potash_ingot = createIngot(materialName);
//			potash_nugget = createNugget(materialName);
			potash_powder = createPowder(materialName);
//			potash_smallblend = createSmallBlend(materialName);
			potash_smallpowder = createSmallPowder(materialName);
		}

		if (Options.ENABLE_SALT) {
			materialName = "salt";

//			salt_blend = createBlend(materialName);
//			salt_ingot = createIngot(materialName);
//			salt_nugget = createNugget(materialName);
			salt_powder = createPowder(materialName);
//			salt_smallblend = createSmallblend(materialName);
			salt_smallpowder = createSmallPowder(materialName);
		}

		if (Options.ENABLE_SALTPETER) {
			materialName = "saltpeter";

//			saltpeter_blend = createBlend(materialName);
//			saltpeter_ingot = createIngot(materialName);
//			saltpeter_nugget = createNugget(materialName);
			saltpeter_powder = createPowder(materialName);
//			saltpeter_smallblend = createSmallBlend(materialName);
			saltpeter_smallpowder = createSmallPowder(materialName);
		}

		if (Options.ENABLE_SILICON) {
			materialName = "silicon";

			silicon_blend = createBlend(materialName);
			silicon_ingot = createIngot(materialName);
			silicon_nugget = createNugget(materialName);
			silicon_powder = createPowder(materialName);
			silicon_smallblend = createSmallBlend(materialName);
			silicon_smallpowder = createSmallPowder(materialName);
		}

		if (Options.ENABLE_SULFUR) {
			materialName = "sulfur";

//			sulfur_blend = createBlend(materialName);
//			sulfur_ingot = createIngot(materialName);
//			sulfur_nugget = createNugget(materialName);
			sulfur_powder = createPowder(materialName);
//			sulfur_smallblend = createSmallBlend(materialName);
			sulfur_smallpowder = createSmallPowder(materialName);
		}

//		addToMetList() // May not be needed, check

		initDone = true;
	}
}
