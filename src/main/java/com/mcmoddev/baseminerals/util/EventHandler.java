package com.mcmoddev.baseminerals.util;

import com.mcmoddev.baseminerals.BaseMinerals;
import com.mcmoddev.lib.init.Materials;
import com.mcmoddev.lib.material.MMDMaterial;

import net.minecraft.item.Item;
import net.minecraft.block.Block;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EventHandler {
	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event) {
		for( MMDMaterial mat : Materials.getMaterialsByMod(BaseMinerals.MODID) ) {
			for( Item item : mat.getItems() ) {
				if( item.getRegistryName().getResourceDomain().equals(BaseMinerals.MODID) ) {
					event.getRegistry().register(item);
				}
			}
		}
	}

	@SubscribeEvent
	public static void registerBlocks(RegistryEvent.Register<Block> event) {
		for( MMDMaterial mat : Materials.getMaterialsByMod(BaseMinerals.MODID) ) {
			for( Block block : mat.getBlocks() ) {
				if( block.getRegistryName().getResourceDomain().equals(BaseMinerals.MODID) ) {
					event.getRegistry().register(block);
				}
			}
		}
	}

}
