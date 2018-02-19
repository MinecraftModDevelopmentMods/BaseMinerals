package com.mcmoddev.baseminerals.init;

import com.mcmoddev.basemetals.init.Materials;
import com.mcmoddev.baseminerals.data.MaterialNames;
import com.mcmoddev.lib.data.Names;
import com.mcmoddev.lib.data.SharedStrings;
import com.mcmoddev.lib.init.MMDCreativeTab;
import com.mcmoddev.lib.material.MMDMaterial;
import com.mcmoddev.lib.util.TabContainer;

import net.minecraft.block.Block;
import net.minecraft.item.Item;

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
		MMDMaterial material = Materials.EMPTY;
		Block blocksTabIconItem;
		Item itemsTabIconItem;

		if (Materials.hasMaterial(MaterialNames.SILICON)) {
			material = Materials.getMaterialByName(MaterialNames.SILICON);
		}

		if (material.isEmpty()) {
			return;
		}

		if (material.hasBlock(Names.BLOCK)) {
			blocksTabIconItem = material.getBlock(Names.BLOCK);
		} else {
			blocksTabIconItem = net.minecraft.init.Blocks.COAL_BLOCK;
		}

		if (material.hasItem(Names.INGOT)) {
			itemsTabIconItem = material.getItem(Names.INGOT);
		} else {
			itemsTabIconItem = net.minecraft.init.Items.COAL;
		}

		blocksTab.setTabIconItem(blocksTabIconItem);
		itemsTab.setTabIconItem(itemsTabIconItem);
	}
}
