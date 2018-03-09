package com.mcmoddev.baseminerals.init;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Nonnull;

import com.mcmoddev.baseminerals.BaseMinerals;
import com.mcmoddev.baseminerals.data.MaterialNames;
import com.mcmoddev.lib.data.Names;
import com.mcmoddev.lib.data.SharedStrings;
import com.mcmoddev.lib.init.Materials;
import com.mcmoddev.lib.item.ItemMMDBlock;
import com.mcmoddev.lib.item.ItemMMDNugget;
import com.mcmoddev.lib.item.ItemMMDPowder;
import com.mcmoddev.lib.item.ItemMMDSmallPowder;
import com.mcmoddev.lib.material.MMDMaterial;
import com.mcmoddev.lib.util.Oredicts;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;

/**
 * This class initializes all items in Base Minerals.
 *
 * @author Jasmine Iwanek
 *
 */
public class Items extends com.mcmoddev.lib.init.Items {

	protected Items() {
		throw new IllegalAccessError(SharedStrings.NOT_INSTANTIABLE);
	}

	/**
	 *
	 */
	public static void init() {
		final List<String> materials = Arrays.asList(MaterialNames.CHARCOAL, MaterialNames.LITHIUM, MaterialNames.NITER,
				MaterialNames.PHOSPHORUS, MaterialNames.POTASH, MaterialNames.SALT, MaterialNames.SALTPETER,
				MaterialNames.SILICON, MaterialNames.SULFUR);

		materials.stream().filter(Materials::hasMaterial)
				.filter(materialName -> !Materials.getMaterialByName(materialName).isEmpty())
				.forEach(materialName -> {
					final MMDMaterial material = Materials.getMaterialByName(materialName);

					create(Names.POWDER, material);
					create(Names.SMALLPOWDER, material);
				});

		Arrays.asList(Names.INGOT, Names.NUGGET).stream()
		.forEach( name -> {
			Arrays.asList(MaterialNames.LITHIUM, MaterialNames.SILICON).stream()
			.filter( material -> !Materials.hasMaterial(material))
			.forEach( material -> create( name, Materials.getMaterialByName(material)));
		});
		
		if (Materials.hasMaterial(MaterialNames.CHARCOAL)) {
			setBurnTimes(MaterialNames.CHARCOAL);
		}

		if (Materials.hasMaterial(MaterialNames.SILICON)) {
			final MMDMaterial silicon = Materials.getMaterialByName(MaterialNames.SILICON);

			create(Names.BLEND, silicon);
			create(Names.SMALLBLEND, silicon);
		}
	}

	private static void setBurnTimes(@Nonnull final String materialName) {
		final MMDMaterial mat = Materials.getMaterialByName(materialName);
		if (mat.isEmpty()) return;
		
		if (mat.hasItem(Names.NUGGET))
			((ItemMMDNugget) mat.getItem(Names.NUGGET)).setBurnTime(160);

		if (mat.hasItem(Names.POWDER))
			((ItemMMDPowder) mat.getItem(Names.POWDER)).setBurnTime(1600);

		if (mat.hasItem(Names.SMALLPOWDER))
			((ItemMMDSmallPowder) mat.getItem(Names.SMALLPOWDER)).setBurnTime(160);

		// simple hack to fix this shit - I give up on trying for more
		if (mat.hasBlock(Names.BLOCK) && mat.getName().equals(MaterialNames.CHARCOAL))
			((ItemMMDBlock) mat.getItem("ItemBlock_charcoal_block")).setBurnTime(16000);
	}

	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event) {
		Materials.getMaterialsByMod(BaseMinerals.MODID).stream()
		.forEach(mat -> regMyItems(event.getRegistry(), mat));

		Oredicts.registerItemOreDictionaryEntries();
		Oredicts.registerBlockOreDictionaryEntries();
	}

	private static Item mapFunc(ItemStack itemStack) {
		return itemStack.getItem();
	}
	
	private static boolean filterFunc(Item item) {
		return item.getRegistryName().getResourceDomain().equals(BaseMinerals.MODID);
	}
	
	private static void regMyItems(IForgeRegistry<Item> registry, MMDMaterial mat) {
		mat.getItems().stream()
		.map(Items::mapFunc)
		.filter(Items::filterFunc)
		.forEach(registry::register);
	}
}
