package baseminerals.init;

import baseminerals.BaseMinerals;
import cyano.basemetals.blocks.*;
import cyano.basemetals.material.MetalMaterial;
import cyano.basemetals.registry.IOreDictionaryEntry;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.BlockSlab;
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
 *
 * @author DrCyano
 *
 */
public abstract class Blocks {

//	protected static Block charcoal_bars;
	protected static Block charcoal_block;
//	protected static Block charcoal_plate;
//	protected static BlockDoor charcoal_door;
//	protected static Block charcoal_ore;
//	protected static Block charcoal_trapdoor;

//	protected static Block lithium_bars;
	protected static Block lithium_block;
//	protected static Block lithium_plate;
//	protected static BlockDoor lithium_door;
	protected static Block lithium_ore;
//	protected static Block lithium_trapdoor;

//	protected static Block niter_bars;
	protected static Block niter_block;
//	protected static Block niter_plate;
//	protected static BlockDoor niter_door;
	protected static Block niter_ore;
//	protected static Block niter_trapdoor;

//	protected static Block phosphorus_bars;
	protected static Block phosphorus_block;
//	protected static Block phosphorus_plate;
//	protected static BlockDoor phosphorus_door;
	protected static Block phosphorus_ore;
//	protected static Block phosphorus_trapdoor;

//	protected static Block potash_bars;
	protected static Block potash_block;
//	protected static Block potash_plate;
//	protected static BlockDoor potash_door;
	protected static Block potash_ore;
//	protected static Block potash_trapdoor;

//	protected static Block salt_bars;
	protected static Block salt_block;
//	protected static Block salt_plate;
//	protected static BlockDoor salt_door;
	protected static Block salt_ore;
//	protected static Block salt_trapdoor;

//	protected static Block saltpeter_bars;
	protected static Block saltpeter_block;
//	protected static Block saltpeter_plate;
//	protected static BlockDoor saltpeter_door;
	protected static Block saltpeter_ore;
//	protected static Block saltpeter_trapdoor;

//	protected static Block silicon_bars;
	protected static Block silicon_block;
//	protected static Block silicon_plate;
//	protected static BlockDoor silicon_door;
//	protected static Block silicon_ore;
//	protected static Block silicon_trapdoor;

//	protected static Block sulfur_bars;
	protected static Block sulfur_block;
//	protected static Block sulfur_plate;
//	protected static BlockDoor sulfur_door;
	protected static Block sulfur_ore;
//	protected static Block sulfur_trapdoor;

	private static boolean initDone = false;

	// private static Map<Block, String> blockRegistry = new HashMap<>();
	private static final Map<String, Block> blockRegistry = new HashMap<>();
	// private static Map<MetalMaterial, List<Block>> blocksByMetal = new HashMap<>();

	/**
	 * Gets an block by its name. The name is the name as it is registered in
	 * the GameRegistry, not its unlocalized name (the unlocalized name is the
	 * registered name plus the prefix "fantasymetals.")
	 * @param name The name of the block in question
	 * @return The block matching that name, or null if there isn't one
	 */
	public static Block getBlockByName(String name) {
		return blockRegistry.get(name);
	}

	/**
	 *
	 */
	public static void init() {
		if(initDone)
			return;

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

		// TODO: Make this support multiple oredicts
		// final block settings
		for(final Block b : blockRegistry.values()) {
			if(b instanceof IOreDictionaryEntry)
				OreDictionary.registerOre(((IOreDictionaryEntry)b).getOreDictionaryName(), b);
			if(!(b instanceof BlockMetalDoor))
				b.setCreativeTab(ItemGroups.tab_blocks);
		}

		initDone = true;
	}

	private static Block addBlock(Block block, String name) {
		final ResourceLocation location = new ResourceLocation(BaseMinerals.MODID, name);
		block.setRegistryName(location);
		block.setUnlocalizedName(location.toString());
		GameRegistry.register(block);

		if (!(block instanceof BlockMetalDoor) && !(block instanceof BlockMetalSlab)) {
			final ItemBlock itemBlock = new ItemBlock(block);
			itemBlock.setRegistryName(location);
			itemBlock.setUnlocalizedName(location.toString());
			GameRegistry.register(itemBlock);
		}

		blockRegistry.put(name, block);
		return block;
	}

	@SuppressWarnings("unused")
	private static Block createPlate(MetalMaterial metal) {
		return addBlock(new BlockMetalPlate(metal), metal.getName() + "_plate");
	}

	@SuppressWarnings("unused")
	private static Block createBars(MetalMaterial metal) {
		return addBlock(new BlockMetalBars(metal), metal.getName() + "_bars");
	}

	private static Block createBlock(MetalMaterial metal) {
		return createBlock(metal, false);
	}

	private static Block createBlock(MetalMaterial metal, boolean glow) {
		return addBlock(new BlockMetalBlock(metal, glow), metal.getName() + "_block");
	}

	@SuppressWarnings("unused")
	private static Block createButton(MetalMaterial metal) {
		return addBlock(new BlockButtonMetal(metal), metal.getName() + "_button");
	}

	@SuppressWarnings("unused")
	private static Block createLever(MetalMaterial metal) {
		return addBlock(new BlockMetalLever(metal), metal.getName() + "_lever");
	}

	@SuppressWarnings("unused")
	private static Block createPressurePlate(MetalMaterial metal) {
		return addBlock(new BlockMetalPressurePlate(metal), metal.getName() + "_pressure_plate");
	}

	@SuppressWarnings("unused")
	private static BlockSlab createSlab(MetalMaterial metal) {
		return (BlockSlab)addBlock(new BlockHalfMetalSlab(metal), metal.getName() + "_slab");
	}

	@SuppressWarnings("unused")
	private static BlockSlab createDoubleSlab(MetalMaterial metal) {
		return (BlockSlab)addBlock(new BlockDoubleMetalSlab(metal), "double_" + metal.getName() + "_slab");
	}

	@SuppressWarnings("unused")
	private static Block createStairs(MetalMaterial metal) {
		return addBlock(new BlockMetalStairs(metal, Blocks.getBlockByName(metal.getName() + "_block")), metal.getName() + "_stairs");
	}

	@SuppressWarnings("unused")
	private static Block createWall(MetalMaterial metal) {
		return addBlock(new BlockMetalWall(metal, Blocks.getBlockByName(metal.getName() + "_block")), metal.getName() + "_wall");
	}

	private static Block createOre(MetalMaterial metal) {
		return addBlock(new BlockMetalOre(metal), metal.getName() + "_ore");
	}

	@SuppressWarnings("unused")
	private static BlockDoor createDoor(MetalMaterial metal) {
		return (BlockDoor)addBlock(new baseminerals.blocks.BlockMetalDoor(metal), metal.getName() + "_door");
	}

	@SuppressWarnings("unused")
	private static Block createTrapDoor(MetalMaterial metal) {
		return addBlock(new BlockMetalTrapDoor(metal), metal.getName() + "_trapdoor");
	}

	/**
	 * 
	 * @param event
	 */
	@SideOnly(Side.CLIENT)
	public static void registerItemRenders(FMLInitializationEvent event) {
		for(final String name : blockRegistry.keySet()) {
			if(blockRegistry.get(name) instanceof BlockDoor)
				continue; // do not add door blocks
			Minecraft.getMinecraft().getRenderItem().getItemModelMesher()
			.register(net.minecraft.item.Item.getItemFromBlock(blockRegistry.get(name)), 0,
				new ModelResourceLocation(new ResourceLocation(BaseMinerals.MODID, name), "inventory"));
		}
	}

	public static Map<String, Block> getBlockRegistry() {
		return blockRegistry;
	}
}
