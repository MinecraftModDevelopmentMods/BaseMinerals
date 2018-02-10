package com.mcmoddev.baseminerals.init;

import com.mcmoddev.baseminerals.data.MaterialNames;
import com.mcmoddev.lib.data.Names;
import com.mcmoddev.lib.data.SharedStrings;
import com.mcmoddev.lib.init.Materials;
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

	private static final int BLOCKS_TAB_ID = addTab(SharedStrings.TAB_BLOCKS, true);
	private static final int ITEMS_TAB_ID = addTab(SharedStrings.TAB_ITEMS, true);
	private static final MMDCreativeTab blocksTab = getTab(BLOCKS_TAB_ID);
	private static final MMDCreativeTab itemsTab = getTab(ITEMS_TAB_ID);
	public static final TabContainer myTabs = new TabContainer(blocksTab, itemsTab, null);

	private ItemGroups() {
		throw new IllegalAccessError(SharedStrings.NOT_INSTANTIABLE);
	}

	/**
	 *
	 */
	public static void init() {
	}
	
	public static void setupIcons() {
		final Item blocksTabIconItem = Item.getItemFromBlock(net.minecraft.init.Blocks.COAL_BLOCK);
		final Item itemsTabIconItem = Materials.hasMaterial(MaterialNames.SILICON) ? Materials.getMaterialByName(MaterialNames.SILICON).getItem(Names.INGOT) : net.minecraft.init.Items.COAL;

		blocksTab.setTabIconItem(new ItemStack(blocksTabIconItem));
		itemsTab.setTabIconItem(new ItemStack(itemsTabIconItem));
	}
}
