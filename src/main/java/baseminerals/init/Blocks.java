package baseminerals.init;

import baseminerals.BaseMinerals;
import cyano.basemetals.blocks.*;
import cyano.basemetals.material.MetalMaterial;
import cyano.basemetals.registry.IOreDictionaryEntry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;

import java.util.HashMap;
import java.util.Map;

/**
 * This class initializes all blocks in Base Minerals and provides some utility 
 * methods for looking up blocks. 
 * @author DrCyano
 *
 */
public abstract class Blocks {

//	public static Block charcoal_bars;
	public static Block charcoal_block;
//	public static Block charcoal_plate;
//	public static BlockDoor charcoal_door;
//	public static Block charcoal_ore;
//	public static Block charcoal_trapdoor;

//	public static Block lithium_bars;
	public static Block lithium_block;
//	public static Block lithium_plate;
//	public static BlockDoor lithium_door;
	public static Block lithium_ore;
//	public static Block lithium_trapdoor;

//	public static Block niter_bars;
	public static Block niter_block;
//	public static Block niter_plate;
//	public static BlockDoor niter_door;
	public static Block niter_ore;
//	public static Block niter_trapdoor;

//	public static Block phosphorus_bars;
	public static Block phosphorus_block;
//	public static Block phosphorus_plate;
//	public static BlockDoor phosphorus_door;
	public static Block phosphorus_ore;
//	public static Block phosphorus_trapdoor;

//	public static Block potash_bars;
	public static Block potash_block;
//	public static Block potash_plate;
//	public static BlockDoor potash_door;
	public static Block potash_ore;
//	public static Block potash_trapdoor;

//	public static Block salt_bars;
	public static Block salt_block;
//	public static Block salt_plate;
//	public static BlockDoor salt_door;
	public static Block salt_ore;
//	public static Block salt_trapdoor;

//	public static Block saltpeter_bars;
	public static Block saltpeter_block;
//	public static Block saltpeter_plate;
//	public static BlockDoor saltpeter_door;
	public static Block saltpeter_ore;
//	public static Block saltpeter_trapdoor;

//	public static Block silicon_bars;
	public static Block silicon_block;
//	public static Block silicon_plate;
//	public static BlockDoor silicon_door;
//	public static Block silicon_ore;
//	public static Block silicon_trapdoor;

//	public static Block sulfur_bars;
	public static Block sulfur_block;
//	public static Block sulfur_plate;
//	public static BlockDoor sulfur_door;
	public static Block sulfur_ore;
//	public static Block sulfur_trapdoor;

	private static boolean initDone = false;

//	private static Map<Block, String> blockRegistry = new HashMap<>();
	private static final Map<String, Block> allBlocks = new HashMap<>();
//	private static Map<MetalMaterial, List<Block>> blocksByMetal = new HashMap<>();

	/**
	 * Gets an block by its name. The name is the name as it is registered in 
	 * the GameRegistry, not its unlocalized name (the unlocalized name is the 
	 * registered name plus the prefix "fantasymetals.")
	 * @param name The name of the block in question
	 * @return The block matching that name, or null if there isn't one
	 */
	public static Block getBlockByName(String name) {
		return allBlocks.get(name);
	}

	/**
	 * 
	 */
	public static void init() {
		if(initDone) return;

		baseminerals.init.Materials.init();
		baseminerals.init.ItemGroups.init();

		charcoal_block = createBlock(Materials.charcoal);
//		charcoal_plate = createPlate(Materials.charcoal);
//		charcoal_ore = createOre(Materials.charcoal);
//		charcoal_bars = createBars(Materials.charcoal);
//		charcoal_door = createDoor(Materials.charcoal);
//		charcoal_trapdoor = createTrapDoor(Materials.charcoal);

		lithium_block = createBlock(Materials.lithium);
//		lithium_plate = createPlate(Materials.lithium);
		lithium_ore = createOre(Materials.lithium);
//		lithium_bars = createBars(Materials.lithium);
//		lithium_door = createDoor(Materials.lithium);
//		lithium_trapdoor = createTrapDoor(Materials.lithium);

		niter_block = createBlock(Materials.niter);
//		niter_plate = createPlate(Materials.niter);
		niter_ore = createOre(Materials.niter);
//		niter_bars = createBars(Materials.niter);
//		niter_door = createDoor(Materials.niter);
//		niter_trapdoor = createTrapDoor(Materials.niter);

		phosphorus_block = createBlock(Materials.phosphorus);
//		phosphorus_plate = createPlate(Materials.phosphorus);
		phosphorus_ore = createOre(Materials.phosphorus);
//		phosphorus_bars = createBars(Materials.phosphorus);
//		phosphorus_door = createDoor(Materials.phosphorus);
//		phosphorus_trapdoor = createTrapDoor(Materials.phosphorus);

		potash_block = createBlock(Materials.potash);
//		potash_plate = createPlate(Materials.potash);
		potash_ore = createOre(Materials.potash);
//		potash_bars = createBars(Materials.potash);
//		potash_door = createDoor(Materials.potash);
//		potash_trapdoor = createTrapDoor(Materials.potash);

		salt_block = createBlock(Materials.salt);
//		salt_plate = createPlate(Materials.salt);
		salt_ore = createOre(Materials.salt);
//		salt_bars = createBars(Materials.salt);
//		salt_door = createDoor(Materials.salt);
//		salt_trapdoor = createTrapDoor(Materials.salt);

		saltpeter_block = createBlock(Materials.saltpeter);
//		saltpeter_plate = createPlate(Materials.saltpeter);
		saltpeter_ore = createOre(Materials.saltpeter);
//		saltpeter_bars = createBars(Materials.saltpeter);
//		saltpeter_door = createDoor(Materials.saltpeter);
//		saltpeter_trapdoor = createTrapDoor(Materials.saltpeter);

		silicon_block = createBlock(Materials.silicon);
//		silicon_plate = createPlate(Materials.silicon);
//		silicon_ore = createOre(Materials.silicon);
//		silicon_bars = createBars(Materials.silicon);
//		silicon_door = createDoor(Materials.silicon);
//		silicon_trapdoor = createTrapDoor(Materials.silicon);

		sulfur_block = createBlock(Materials.sulfur);
//		sulfur_plate = createPlate(Materials.sulfur);
		sulfur_ore = createOre(Materials.sulfur);
//		sulfur_bars = createBars(Materials.sulfur);
//		sulfur_door = createDoor(Materials.sulfur);
//		sulfur_trapdoor = createTrapDoor(Materials.sulfur);

		// final block settings
		for(Block b : allBlocks.values()) {
			if(b instanceof IOreDictionaryEntry) { OreDictionary.registerOre(((IOreDictionaryEntry)b).getOreDictionaryName(), b); }
			if(!(b instanceof BlockMetalDoor)) b.setCreativeTab(ItemGroups.tab_blocks);
		}

		initDone = true;
	}

	private static Block addBlock(Block block, String name) {
		ResourceLocation location = new ResourceLocation(BaseMinerals.MODID, name);
		block.setRegistryName(location);
		block.setUnlocalizedName(location.toString());
		GameRegistry.register(block);

		if (!(block instanceof BlockMetalDoor)) {
			ItemBlock itemBlock = new ItemBlock(block);
			itemBlock.setRegistryName(location);
			itemBlock.setUnlocalizedName(location.toString());
			GameRegistry.register(itemBlock);
		}

		allBlocks.put(name, block);
		return block;
	}

	private static Block createPlate(MetalMaterial metal) {
		return addBlock(new BlockMetalPlate(metal), metal.getName()+"_plate");
	}

	private static Block createBars(MetalMaterial metal) {
		return addBlock(new BlockMetalBars(metal), metal.getName()+"_bars");
	}

	private static Block createBlock(MetalMaterial metal) {
		return createBlock(metal, false);
	}

	private static Block createBlock(MetalMaterial metal, boolean glow) {
		return addBlock(new BlockMetalBlock(metal, glow), metal.getName()+"_block");
	}

	private static Block createOre(MetalMaterial metal) {
		return addBlock(new BlockMetalOre(metal), metal.getName()+"_ore");
	}

	private static BlockDoor createDoor(MetalMaterial metal) {
		return (BlockDoor)addBlock(new baseminerals.blocks.BlockMetalDoor(metal), metal.getName()+"_door");
	}

	private static Block createTrapDoor(MetalMaterial metal) {
		return addBlock(new BlockMetalTrapDoor(metal), metal.getName()+"_trapdoor");
	}

	/**
	 * 
	 * @param event
	 */
	@SideOnly(Side.CLIENT)
	public static void registerItemRenders(FMLInitializationEvent event) {
		for(String name : allBlocks.keySet()) {
			if(allBlocks.get(name) instanceof BlockDoor) continue; // do not add door blocks
			Minecraft.getMinecraft().getRenderItem().getItemModelMesher()
			.register(net.minecraft.item.Item.getItemFromBlock(allBlocks.get(name)), 0, 
				new ModelResourceLocation(new ResourceLocation(BaseMinerals.MODID, name), "inventory"));
		}
	}
}
