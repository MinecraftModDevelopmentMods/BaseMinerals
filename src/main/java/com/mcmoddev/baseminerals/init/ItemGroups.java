package com.mcmoddev.baseminerals.init;

import com.mcmoddev.basemetals.init.Materials;
import com.mcmoddev.lib.data.Names;
import com.mcmoddev.lib.init.MMDCreativeTab;

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
	private static final int blocksTabId  = addTab("blocks", true );;
	private static final int itemsTabId = addTab("items", true );
	public static final MMDCreativeTab blocksTab = getTab(blocksTabId);
	public static final MMDCreativeTab itemsTab = getTab(itemsTabId);

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
		Item itemsTabIconItem = Materials.getMaterialByName("silicon")!=null?Materials.getMaterialByName("silicon").getItem(Names.INGOT):net.minecraft.init.Items.COAL;

		blocksTab.setTabIconItem(new ItemStack(blocksTabIconItem));
		itemsTab.setTabIconItem(new ItemStack(itemsTabIconItem));
	}
}
