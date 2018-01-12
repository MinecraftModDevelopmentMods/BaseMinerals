package com.mcmoddev.baseminerals.init;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Nonnull;

import com.mcmoddev.baseminerals.data.MaterialNames;
import com.mcmoddev.lib.data.Names;
import com.mcmoddev.lib.data.SharedStrings;
import com.mcmoddev.lib.init.Materials;
import com.mcmoddev.lib.item.ItemMMDNugget;
import com.mcmoddev.lib.item.ItemMMDPowder;
import com.mcmoddev.lib.item.ItemMMDSmallPowder;
import com.mcmoddev.lib.material.MMDMaterial;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

/**
 * This class initializes all items in Base Minerals.
 *
 * @author Jasmine Iwanek
 *
 */
public class Items extends com.mcmoddev.lib.init.Items {

	private static boolean initDone = false;

	protected Items() {
		throw new IllegalAccessError(SharedStrings.NOT_INSTANTIABLE);
	}

	/**
	 *
	 */
	public static void init() {
		if (initDone) {
			return;
		}

		Blocks.init();

		final List<String> materials = Arrays.asList(MaterialNames.CHARCOAL, MaterialNames.LITHIUM, MaterialNames.NITER,
				MaterialNames.PHOSPHORUS, MaterialNames.POTASH, MaterialNames.SALT, MaterialNames.SALTPETER,
				MaterialNames.SILICON, MaterialNames.SULFUR);

		materials.stream().filter(Materials::hasMaterial)
				.filter(name -> !Materials.getMaterialByName(name).equals(Materials.emptyMaterial)).forEach(name -> {
					final MMDMaterial material = Materials.getMaterialByName(name);

					create(Names.POWDER, material);
					create(Names.SMALLPOWDER, material);
				});

		if (Materials.hasMaterial(MaterialNames.CHARCOAL)) {
			final MMDMaterial charcoal = Materials.getMaterialByName(MaterialNames.CHARCOAL);

			create(Names.NUGGET, charcoal); // Special

			if (charcoal.hasItem(Names.NUGGET)) {
				((ItemMMDNugget) charcoal.getItem(Names.NUGGET)).setBurnTime(200);
			}
			if (charcoal.hasItem(Names.POWDER)) {
				((ItemMMDPowder) charcoal.getItem(Names.POWDER)).setBurnTime(1600);
			}
			if (charcoal.hasItem(Names.SMALLPOWDER)) {
				((ItemMMDSmallPowder) charcoal.getItem(Names.SMALLPOWDER)).setBurnTime(200);
			}
		}

		if (Materials.hasMaterial(MaterialNames.LITHIUM)) {
			final MMDMaterial lithium = Materials.getMaterialByName(MaterialNames.LITHIUM);

			create(Names.INGOT, lithium);
			create(Names.NUGGET, lithium);
		}

		if (Materials.hasMaterial(MaterialNames.SILICON)) {
			final MMDMaterial silicon = Materials.getMaterialByName(MaterialNames.SILICON);

			create(Names.BLEND, silicon);
			create(Names.INGOT, silicon);
			create(Names.NUGGET, silicon);
			create(Names.SMALLBLEND, silicon);
		}

		initDone = true;
	}

	private static Item create(@Nonnull final Names name, @Nonnull final MMDMaterial material) {
		CreativeTabs tab;

		if ((name.equals(Names.DOOR)) || (name.equals(Names.SLAB))) {
			tab = ItemGroups.myTabs.blocksTab;
		} else if ((name.equals(Names.BLEND)) || (name.equals(Names.INGOT)) || (name.equals(Names.NUGGET)) || (name.equals(Names.POWDER)) || (name.equals(Names.SMALLBLEND)) || (name.equals(Names.SMALLPOWDER)) || (name.equals(Names.ROD)) || (name.equals(Names.GEAR))) {
			tab = ItemGroups.myTabs.itemsTab;
		} else {
			tab = ItemGroups.myTabs.toolsTab;
		}

		return create(name, material, tab);
	}
}
