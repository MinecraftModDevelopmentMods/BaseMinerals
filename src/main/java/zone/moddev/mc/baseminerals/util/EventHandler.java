package zone.moddev.mc.baseminerals.util;

import zone.moddev.mc.baseminerals.BaseMinerals;
import zone.moddev.mc.baseminerals.init.Blocks;
import zone.moddev.mc.baseminerals.init.Fluids;
import zone.moddev.mc.baseminerals.init.Items;
import zone.moddev.mc.baseminerals.init.Materials;
import com.mcmoddev.lib.events.*;

import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import net.minecraft.block.Block;
import net.minecraft.item.Item;

@EventBusSubscriber
public class EventHandler {
	@SubscribeEvent
	public static void mmdlibRegisterMaterials(MMDLibRegisterMaterials ev) {
		Materials.init();
	}

	@SubscribeEvent
	public static void mmdlibRegisterBlocks(MMDLibRegisterBlocks ev) {
		Blocks.init();
	}

	@SubscribeEvent
	public static void mmdlibRegisterItems(MMDLibRegisterItems ev) {
		Items.init();
	}

	@SubscribeEvent
	public static void mmdlibRegisterFluids(MMDLibRegisterFluids ev) {
		Fluids.init();
	}

	@SubscribeEvent
	public static void onRemapBlock(RegistryEvent.MissingMappings<Block> event) {
		for (final RegistryEvent.MissingMappings.Mapping<Block> mapping : event.getAllMappings()) {
			if (mapping.key.getNamespace().equals(BaseMinerals.MODID)) {
				// dummy
			}
		}
	}

	@SubscribeEvent
	public static void onRemapItem(RegistryEvent.MissingMappings<Item> event) {
		for (final RegistryEvent.MissingMappings.Mapping<Item> mapping : event.getAllMappings()) {
			if (mapping.key.getNamespace().equals(BaseMinerals.MODID)) {
				// dummy
			}
		}
	}

}
