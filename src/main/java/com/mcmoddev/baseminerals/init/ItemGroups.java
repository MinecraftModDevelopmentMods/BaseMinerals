package com.mcmoddev.baseminerals.init;

import com.mcmoddev.lib.init.Materials;
import com.mcmoddev.baseminerals.BaseMinerals;
import com.mcmoddev.lib.data.Names;
import com.mcmoddev.lib.data.SharedStrings;
import com.mcmoddev.lib.material.MMDMaterial;

/**
 * This class initializes all item groups in Base Minerals.
 *
 * @author Jasmine Iwanek
 *
 */
public class ItemGroups extends com.mcmoddev.lib.init.ItemGroups {

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

			if (material.hasBlock(Names.BLOCK)) {
				getTab(BaseMinerals.MODID,SharedStrings.TAB_BLOCKS).setTabIconItem(material.getBlock(Names.BLOCK));
			}

			if (material.hasItem(Names.POWDER)) {
				getTab(BaseMinerals.MODID,SharedStrings.TAB_ITEMS).setTabIconItem(material.getItem(Names.POWDER));
			}
		}
	}
}
