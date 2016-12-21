package mmd.baseminerals.init;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.*;
import net.minecraftforge.fml.common.Loader;
import cyano.basemetals.init.GeneralizedCreativeTab;
import mmd.baseminerals.init.Items;

/**
 * This class initializes all item groups in Base Minerals.
 *
 * @author DrCyano
 *
 */
public class ItemGroups {

	public static final java.util.function.BiFunction<ItemStack, ItemStack, Integer> sortingAlgorithm = (ItemStack a, ItemStack b) -> {
		final int delta = Items.getSortingValue(a) - Items.getSortingValue(b);
		if (delta == 0) {
			return a.getItem().getUnlocalizedName().compareToIgnoreCase(b.getItem().getUnlocalizedName());
		}
		return delta;
	};

	public static CreativeTabs tab_blocks;
	public static CreativeTabs tab_items;
	public static CreativeTabs tab_tools;

	private static boolean initDone = false;

	/**
	 *
	 */
	public static void init() {
		if (initDone) {
			return;
		}

		// placeholders
		// Item blockItem = Items.charcoal_powder;
		// Item itemItem = Items.charcoal_powder;
		// Item toolItem = Items.charcoal_powder;

		tab_blocks = new GeneralizedCreativeTab(Loader.instance().activeModContainer().getModId().concat(".blocks"), true, () -> Items.charcoal_powder);

		tab_items = tab_blocks;
		tab_tools = tab_items;

		initDone = true;
	}
}
