package com.mcmoddev.baseminerals.init;

import com.mcmoddev.basemetals.init.Materials;
import com.mcmoddev.baseminerals.data.MaterialNames;
import com.mcmoddev.lib.data.Names;
import com.mcmoddev.lib.init.MMDCreativeTab;
import com.mcmoddev.lib.util.TabContainer;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

/**
 * This class initializes all item groups in Base Minerals.
 *
 * @author Jasmine Iwanek
 *
 */
public class ItemGroups extends com.mcmoddev.lib.init.ItemGroups {

	private static boolean initDone = false;
	private static final int BLOCKS_TAB_ID  = addTab("blocks", true);
	private static final int ITEMS_TAB_ID = addTab("items", true);
	private static final MMDCreativeTab blocksTab = getTab(BLOCKS_TAB_ID);
	private static final MMDCreativeTab itemsTab = getTab(ITEMS_TAB_ID);
	public static final TabContainer myTabs = new TabContainer(blocksTab, itemsTab, null);

	private ItemGroups() {
		throw new IllegalAccessError("Not a instantiable class");
	}

	/**
	 *
	 */
	public static void init() {
		if (initDone) {
			return;
		}

		initDone = true;
	}
	
	public static void setupIcons() {
		Item blocksTabIconItem = Item.getItemFromBlock(net.minecraft.init.Blocks.COAL_BLOCK);
		Item itemsTabIconItem = Materials.getMaterialByName(MaterialNames.SILICON) != null ? Materials.getMaterialByName(MaterialNames.SILICON).getItem(Names.INGOT) : net.minecraft.init.Items.COAL;

		blocksTab.setTabIconItem(new ItemStack(blocksTabIconItem));
		itemsTab.setTabIconItem(new ItemStack(itemsTabIconItem));
	}
}
