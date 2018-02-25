package com.mcmoddev.baseminerals.init;

import com.mcmoddev.lib.init.Materials;
import com.mcmoddev.lib.data.Names;
import com.mcmoddev.lib.data.SharedStrings;
import com.mcmoddev.lib.init.MMDCreativeTab;
import com.mcmoddev.lib.material.MMDMaterial;

/**
 * This class initializes all item groups in Base Minerals.
 *
 * @author Jasmine Iwanek
 *
 */
public class ItemGroups extends com.mcmoddev.lib.init.ItemGroups {

	private static final MMDCreativeTab blocksTab = addTab(SharedStrings.TAB_BLOCKS);
	private static final MMDCreativeTab itemsTab = addTab(SharedStrings.TAB_ITEMS);

	private ItemGroups() {
		throw new IllegalAccessError(SharedStrings.NOT_INSTANTIABLE);
	}

	/**
	 *
	 */
	public static void init() {
	}

	public static void setupIcons(String materialName) {
		if (Materials.hasMaterial(materialName)) {
			final MMDMaterial material = Materials.getMaterialByName(materialName);

			if ((blocksTab != null) && (material.hasBlock(Names.BLOCK))) {
				blocksTab.setTabIconItem(material.getBlock(Names.BLOCK));
			}

			if ((itemsTab != null) && (material.hasItem(Names.POWDER))) {
				itemsTab.setTabIconItem(material.getItem(Names.POWDER));
			}
		}
	}
}
