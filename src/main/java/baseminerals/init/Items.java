package baseminerals.init;

import baseminerals.BaseMinerals;
import cyano.basemetals.blocks.*;
import cyano.basemetals.items.*;
import cyano.basemetals.material.IMetalObject;
import cyano.basemetals.material.MetalMaterial;
import cyano.basemetals.registry.IOreDictionaryEntry;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.BlockSlab;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;

import java.util.*;

/**
 * This class initializes all items in Base Minerals and provides some utility 
 * methods for looking up items. 
 * @author DrCyano
 *
 */
public abstract class Items {

//	protected static Item charcoal_blend;
//	protected static Item charcoal_ingot;
	protected static Item charcoal_nugget;
	protected static Item charcoal_powder;
//	protected static Item charcoal_smallblend;
	protected static Item charcoal_smallpowder;

//	protected static Item lithium_blend;
	protected static Item lithium_ingot;
	protected static Item lithium_nugget;
	protected static Item lithium_powder;
//	protected static Item lithium_smallblend;
	protected static Item lithium_smallpowder;

//	protected static Item niter_blend;
//	protected static Item niter_ingot;
//	protected static Item niter_nugget;
	protected static Item niter_powder;
//	protected static Item niter_smallblend;
	protected static Item niter_smallpowder;

//	protected static Item phosphorus_blend;
//	protected static Item phosphorus_ingot;
//	protected static Item phosphorus_nugget;
	protected static Item phosphorus_powder;
//	protected static Item phosphorus_smallblend;
	protected static Item phosphorus_smallpowder;

//	protected static Item potash_blend;
//	protected static Item potash_ingot;
//	protected static Item potash_nugget;
	protected static Item potash_powder;
//	protected static Item potash_smallblend;
	protected static Item potash_smallpowder;

//	protected static Item salt_blend;
//	protected static Item salt_ingot;
//	protected static Item salt_nugget;
	protected static Item salt_powder;
//	protected static Item salt_smallblend;
	protected static Item salt_smallpowder;

//	protected static Item saltpeter_blend;
//	protected static Item saltpeter_ingot;
//	protected static Item saltpeter_nugget;
	protected static Item saltpeter_powder;
//	protected static Item saltpeter_smallblend;
	protected static Item saltpeter_smallpowder;

	protected static Item silicon_blend;
	protected static Item silicon_ingot;
	protected static Item silicon_nugget;
	protected static Item silicon_powder;
	protected static Item silicon_smallblend;
	protected static Item silicon_smallpowder;

//	protected static Item sulfur_blend;
//	protected static Item sulfur_ingot;
//	protected static Item sulfur_nugget;
	protected static Item sulfur_powder;
//	protected static Item sulfur_smallblend;
	protected static Item sulfur_smallpowder;

	private static boolean initDone = false;

	private static Map<Item, String> itemRegistry = new HashMap<>();
	private static Map<String, Item> allItems = new HashMap<>();
	private static Map<MetalMaterial, List<Item>> itemsByMetal = new HashMap<>();

	private static Map<BlockDoor, Item> doorMap = new HashMap<>();

	private static Map<Class<?>, Integer> classSortingValues = new HashMap<>();
	private static Map<MetalMaterial, Integer> materialSortingValues = new HashMap<>();

	/**
	 * Gets an item by its name. The name is the name as it is registered in the
	 * GameRegistry, not its unlocalized name (the unlocalized name is the
	 * registered name plus the prefix "modernmetals.")
	 * @param name The name of the item in question
	 * @return The item matching that name, or null if there isn't one
	 */
	public static Item getItemByName(String name) {
		return allItems.get(name);
	}

	/**
	 * This is the reverse of the getItemByName(...) method, returning the
	 * registered name of an item instance (Modern Metals items only).
	 * @param i The item in question
	 * @return The name of the item, or null if the item is not a Modern Metals
	 * item.
	 */
	public static String getNameOfItem(Item i) {
		return itemRegistry.get(i);
	}

	/**
	 * Gets a map of all items added, sorted by metal
	 * @return An unmodifiable map of added items catagorized by metal material
	 */
	public static Map<MetalMaterial, List<Item>> getItemsByMetal() {
		return Collections.unmodifiableMap(itemsByMetal);
	}

	/**
	 * Gets the inventory item corresponding to a given door block
	 *
	 * @param b The door block
	 * @return The item that the player should use to place that kind of door
	 */
	public static Item getDoorItemForBlock(BlockMetalDoor b) {
		return doorMap.get(b);
	}

	/**
	 *
	 */
	public static void init() {
		if(initDone)
			return;
		
		baseminerals.init.Blocks.init();

//		charcoal_blend = createBlend(Materials.charcoal);
//		charcoal_ingot = createIngot(Materials.charcoal);
		charcoal_nugget = createNugget(Materials.charcoal); // Special
		charcoal_powder = createPowder(Materials.charcoal);
//		charcoal_smallblend = createSmallBlend(Materials.charcoal);
		charcoal_smallpowder = createSmallPowder(Materials.charcoal);

//		lithium_blend = createBlend(Materials.lithium);
		lithium_ingot = createIngot(Materials.lithium);
		lithium_nugget = createNugget(Materials.lithium);
		lithium_powder = createPowder(Materials.lithium);
//		lithium_smallblend = createSmallBlend(Materials.lithium);
		lithium_smallpowder = createSmallPowder(Materials.lithium);

//		niter_blend = createBlend(Materials.niter);
//		niter_ingot = createIngot(Materials.niter);
//		niter_nugget = createNugget(Materials.niter);
		niter_powder = createPowder(Materials.niter);
//		niter_smallblend = createSmallBlend(Materials.niter);
		niter_smallpowder = createSmallPowder(Materials.niter);

//		phosphorus_blend = createBlend(Materials.phosphorus);
//		phosphorus_ingot = createIngot(Materials.phosphorus);
//		phosphorus_nugget = createNugget(Materials.phosphorus);
		phosphorus_powder = createPowder(Materials.phosphorus);
//		phosphorus_smallblend = createSmallBlend(Materials.phosphorus);
		phosphorus_smallpowder = createSmallPowder(Materials.phosphorus);

//		potash_blend = createBlend(Materials.potash);
//		potash_ingot = createIngot(Materials.potash);
//		potash_nugget = createNugget(Materials.potash);
		potash_powder = createPowder(Materials.potash);
//		potash_smallblend = createSmallBlend(Materials.potash);
		potash_smallpowder = createSmallPowder(Materials.potash);

//		salt_blend = createBlend(Materials.salt);
//		salt_ingot = createIngot(Materials.salt);
//		salt_nugget = createNugget(Materials.salt);
		salt_powder = createPowder(Materials.salt);
//		salt_smallblend = createSmallblend(Materials.salt);
		salt_smallpowder = createSmallPowder(Materials.salt);

//		saltpeter_blend = createBlend(Materials.saltpeter);
//		saltpeter_ingot = createIngot(Materials.saltpeter);
//		saltpeter_nugget = createNugget(Materials.saltpeter);
		saltpeter_powder = createPowder(Materials.saltpeter);
//		saltpeter_smallblend = createSmallBlend(Materials.saltpeter);
		saltpeter_smallpowder = createSmallPowder(Materials.saltpeter);

		silicon_blend = createBlend(Materials.silicon);
		silicon_ingot = createIngot(Materials.silicon);
		silicon_nugget = createNugget(Materials.silicon);
		silicon_powder = createPowder(Materials.silicon);
		silicon_smallblend = createSmallBlend(Materials.silicon);
		silicon_smallpowder = createSmallPowder(Materials.silicon);

//		sulfur_blend = createBlend(Materials.sulfur);
//		sulfur_ingot = createIngot(Materials.sulfur);
//		sulfur_nugget = createNugget(Materials.sulfur);
		sulfur_powder = createPowder(Materials.sulfur);
//		sulfur_smallblend = createSmallBlend(Materials.sulfur);
		sulfur_smallpowder = createSmallPowder(Materials.sulfur);

		// TODO: Make this support multiple oredicts
		for(final Item i : itemRegistry.keySet()) {
			allItems.put(itemRegistry.get(i), i);
			if(i instanceof IOreDictionaryEntry)
				OreDictionary.registerOre(((IOreDictionaryEntry)i).getOreDictionaryName(), i);
		}

		int ss = 0;
		classSortingValues.put(BlockMetalOre.class, ++ss * 10000);
		classSortingValues.put(BlockMetalBlock.class, ++ss * 10000);
		classSortingValues.put(BlockMetalPlate.class, ++ss * 10000);
		classSortingValues.put(BlockMetalBars.class, ++ss * 10000);
		classSortingValues.put(BlockMetalDoor.class, ++ss * 10000);
		classSortingValues.put(BlockMetalTrapDoor.class, ++ss * 10000);
		classSortingValues.put(InteractiveFluidBlock.class, ++ss * 10000);
		classSortingValues.put(baseminerals.items.ItemMetalIngot.class, ++ss * 10000);
		classSortingValues.put(ItemMetalNugget.class, ++ss * 10000);
		classSortingValues.put(ItemMetalPowder.class, ++ss * 10000);
		classSortingValues.put(baseminerals.items.ItemMetalBlend.class, classSortingValues.get(ItemMetalPowder.class));
		classSortingValues.put(ItemMetalSmallPowder.class, ++ss * 10000);
		classSortingValues.put(ItemMetalSmallBlend.class, classSortingValues.get(ItemMetalSmallPowder.class));
		classSortingValues.put(ItemMetalCrackHammer.class, ++ss * 10000);
		classSortingValues.put(ItemMetalPickaxe.class, ++ss * 10000);
		classSortingValues.put(ItemMetalShovel.class, ++ss * 10000);
		classSortingValues.put(ItemMetalAxe.class, ++ss * 10000);
		classSortingValues.put(ItemMetalHoe.class, ++ss * 10000);
		classSortingValues.put(ItemMetalSword.class, ++ss * 10000);
		classSortingValues.put(ItemMetalArmor.class, ++ss * 10000);
		classSortingValues.put(GenericMetalItem.class, ++ss * 10000);
		classSortingValues.put(ItemMetalArrow.class, ++ss * 10000);
		classSortingValues.put(ItemMetalBolt.class, ++ss * 10000);
		classSortingValues.put(ItemMetalBow.class, ++ss * 10000);
		classSortingValues.put(ItemMetalCrossbow.class, ++ss * 10000);
		classSortingValues.put(ItemMetalFishingRod.class, ++ss * 10000);
		classSortingValues.put(ItemMetalHorseArmor.class, ++ss * 10000);
		classSortingValues.put(ItemMetalShears.class, ++ss * 10000);
		classSortingValues.put(ItemMetalShield.class, ++ss * 10000);
		classSortingValues.put(ItemMetalDoor.class, classSortingValues.get(BlockMetalDoor.class));

		classSortingValues.put(BlockButtonMetal.class, ++ss * 10000);
		classSortingValues.put(BlockMetalSlab.class, ++ss * 10000);
		classSortingValues.put(BlockDoubleMetalSlab.class, ++ss * 10000); // Probably not needed
		classSortingValues.put(BlockHalfMetalSlab.class, ++ss * 10000); // Probably not needed
		classSortingValues.put(BlockMetalLever.class, ++ss * 10000);
		classSortingValues.put(BlockMetalPressurePlate.class, ++ss * 10000);
		classSortingValues.put(BlockMetalStairs.class, ++ss * 10000);
		classSortingValues.put(BlockMetalWall.class, ++ss * 10000);
		classSortingValues.put(BlockMoltenFluid.class, ++ss * 10000);
		classSortingValues.put(ItemMetalSlab.class, ++ss * 10000);

		List<MetalMaterial> metlist = new ArrayList<>(Materials.getAllMetals().size());
		metlist.addAll(Materials.getAllMetals());
		metlist.sort((MetalMaterial a, MetalMaterial b)-> a.getName().compareToIgnoreCase(b.getName()));
		for(int i = 0; i < metlist.size(); i++)
			materialSortingValues.put(metlist.get(i), i * 100);

		initDone = true;
	}

	private static Item registerItem(Item item, String name, MetalMaterial metal, CreativeTabs tab) {
		final ResourceLocation location = new ResourceLocation(BaseMinerals.MODID, name);
		item.setRegistryName(location);
		item.setUnlocalizedName(location.toString());
		GameRegistry.register(item);
		itemRegistry.put(item, name);

		if(tab != null) {
			item.setCreativeTab(tab);
		}

		if(metal != null) {
			itemsByMetal.computeIfAbsent(metal, (MetalMaterial g)->new ArrayList<>());
			itemsByMetal.get(metal).add(item);
		}

		return item;
	}

	private static Item createIngot(MetalMaterial metal) {
		return registerItem(new baseminerals.items.ItemMetalIngot(metal), metal.getName() + "_ingot", metal, ItemGroups.tab_items);
	}

	private static Item createNugget(MetalMaterial metal) {
		return registerItem(new ItemMetalNugget(metal), metal.getName() + "_nugget", metal, ItemGroups.tab_items);
	}

	private static Item createPowder(MetalMaterial metal) {
		return registerItem(new ItemMetalPowder(metal), metal.getName() + "_powder", metal, ItemGroups.tab_items);
	}

	private static Item createBlend(MetalMaterial metal) {
		return registerItem(new baseminerals.items.ItemMetalBlend(metal), metal.getName() + "_blend", metal, ItemGroups.tab_items);
	}

	@SuppressWarnings("unused")
	private static Item createRod(MetalMaterial metal) {
		return registerItem(new GenericMetalItem(metal), metal.getName() + "_rod", metal, ItemGroups.tab_items);
	}

	@SuppressWarnings("unused")
	private static Item createGear(MetalMaterial metal) {
		Item i = registerItem(new GenericMetalItem(metal), metal.getName() + "_gear", metal, ItemGroups.tab_items);
		OreDictionary.registerOre("gear" + metal.getCapitalizedName(), i);
		return i;
	}

	@SuppressWarnings("unused")
	private static Item createAxe(MetalMaterial metal) {
		return registerItem(new ItemMetalAxe(metal), metal.getName() + "_axe", metal, ItemGroups.tab_tools);
	}

	@SuppressWarnings("unused")
	private static Item createCrackhammer(MetalMaterial metal) {
		return registerItem(new ItemMetalCrackHammer(metal), metal.getName() + "_crackhammer", metal, ItemGroups.tab_tools);
	}

	@SuppressWarnings("unused")
	private static Item createHoe(MetalMaterial metal) {
		return registerItem(new ItemMetalHoe(metal), metal.getName() + "_hoe", metal, ItemGroups.tab_tools);
	}

	@SuppressWarnings("unused")
	private static Item createPickaxe(MetalMaterial metal) {
		return registerItem(new ItemMetalPickaxe(metal), metal.getName() + "_pickaxe", metal, ItemGroups.tab_tools);
	}

	@SuppressWarnings("unused")
	private static Item createShovel(MetalMaterial metal) {
		return registerItem(new ItemMetalShovel(metal), metal.getName() + "_shovel", metal, ItemGroups.tab_tools);
	}

	@SuppressWarnings("unused")
	private static Item createSword(MetalMaterial metal) {
		return registerItem(new ItemMetalSword(metal), metal.getName() + "_sword", metal, ItemGroups.tab_tools);
	}

	@SuppressWarnings("unused")
	private static Item createHelmet(MetalMaterial metal) {
		return registerItem(ItemMetalArmor.createHelmet(metal), metal.getName() + "_helmet", metal, ItemGroups.tab_tools);
	}

	@SuppressWarnings("unused")
	private static Item createChestplate(MetalMaterial metal) {
		return registerItem(ItemMetalArmor.createChestplate(metal), metal.getName() + "_chestplate", metal, ItemGroups.tab_tools);
	}

	@SuppressWarnings("unused")
	private static Item createLeggings(MetalMaterial metal) {
		return registerItem(ItemMetalArmor.createLeggings(metal), metal.getName() + "_leggings", metal, ItemGroups.tab_tools);
	}

	@SuppressWarnings("unused")
	private static Item createBoots(MetalMaterial metal) {
		return registerItem(ItemMetalArmor.createBoots(metal), metal.getName() + "_boots", metal, ItemGroups.tab_tools);
	}

	@SuppressWarnings("unused")
	private static Item createHorseArmor(MetalMaterial metal) {
		return registerItem(new ItemMetalHorseArmor(metal), metal.getName() + "_horsearmor", metal, ItemGroups.tab_tools);
	}

	@SuppressWarnings("unused")
	private static Item createArrow(MetalMaterial metal) {
		return registerItem(new ItemMetalArrow(metal), metal.getName() + "_arrow", metal, ItemGroups.tab_tools);
	}

	@SuppressWarnings("unused")
	private static Item createBolt(MetalMaterial metal) {
		return registerItem(new ItemMetalBolt(metal), metal.getName() + "_bolt", metal, ItemGroups.tab_tools);
	}

	@SuppressWarnings("unused")
	private static Item createBow(MetalMaterial metal) {
		return registerItem(new ItemMetalBow(metal), metal.getName() + "_bow", metal, ItemGroups.tab_tools);
	}

	@SuppressWarnings("unused")
	private static Item createCrossbow(MetalMaterial metal) {
		return registerItem(new ItemMetalCrossbow(metal), metal.getName() + "_crossbow", metal, ItemGroups.tab_tools);
	}

	@SuppressWarnings("unused")
	private static Item createShears(MetalMaterial metal) {
		return registerItem(new ItemMetalShears(metal), metal.getName() + "_shears", metal, ItemGroups.tab_tools);
	}

	private static Item createSmallBlend(MetalMaterial metal) {
		return registerItem(new ItemMetalSmallBlend(metal), metal.getName() + "_smallblend", metal, ItemGroups.tab_items);
	}

	@SuppressWarnings("unused")
	private static Item createFishingRod(MetalMaterial metal) {
		return registerItem(new ItemMetalFishingRod(metal), metal.getName() + "_fishing_rod", metal, ItemGroups.tab_tools);
	}

	private static Item createSmallPowder(MetalMaterial metal) {
		return registerItem(new ItemMetalSmallPowder(metal), metal.getName() + "_smallpowder", metal, ItemGroups.tab_items);
	}


	@SuppressWarnings("unused")
	private static Item createShield(MetalMaterial metal) {
		return registerItem(new ItemMetalShield(metal), metal.getName() + "_shield", metal, ItemGroups.tab_items);
	}

	@SuppressWarnings("unused")
	private static Item createCrystal(MetalMaterial metal) {
		final Item i = registerItem(new GenericMetalItem(metal), metal.getName() + "_crystal", metal, ItemGroups.tab_items);
		OreDictionary.registerOre("crystal" + metal.getCapitalizedName(), i);
		return i;
	}

	@SuppressWarnings("unused")
	private static Item createShard(MetalMaterial metal) {
		final Item i = registerItem(new GenericMetalItem(metal), metal.getName() + "_shard", metal, ItemGroups.tab_items);
		OreDictionary.registerOre("shard" + metal.getCapitalizedName(), i);
		return i;
	}

	@SuppressWarnings("unused")
	private static Item createClump(MetalMaterial metal) {
		final Item i = registerItem(new GenericMetalItem(metal), metal.getName() + "_clump", metal, ItemGroups.tab_items);
		OreDictionary.registerOre("clump" + metal.getCapitalizedName(), i);
		return i;
	}

	@SuppressWarnings("unused")
	private static Item createDirtyPowder(MetalMaterial metal) {
		final Item i = registerItem(new GenericMetalItem(metal), metal.getName() + "_powder_dirty", metal, ItemGroups.tab_items);
		OreDictionary.registerOre("dustDirty" + metal.getCapitalizedName(), i);
		return i;
	}

	// TODO: Possibly make this a block, 1/2 of the normal plate.
	@SuppressWarnings("unused")
	private static Item createDensePlate(MetalMaterial metal) {
		final Item i = registerItem(new GenericMetalItem(metal), metal.getName() + "_dense_plate", metal, ItemGroups.tab_items);
		OreDictionary.registerOre("plateDense" + metal.getCapitalizedName(), i);
		return i;
	}

	@SuppressWarnings("unused")
	private static Item createCrushed(MetalMaterial metal) {
		final Item i = registerItem(new GenericMetalItem(metal), metal.getName() + "_crushed", metal, ItemGroups.tab_items);
		OreDictionary.registerOre("crushed" + metal.getCapitalizedName(), i);
		return i;
	}

	@SuppressWarnings("unused")
	private static Item createCrushedPurified(MetalMaterial metal) {
		final Item i = registerItem(new GenericMetalItem(metal), metal.getName() + "_crushed_purified", metal, ItemGroups.tab_items);
		OreDictionary.registerOre("crushedPurified" + metal.getCapitalizedName(), i);
		return i;
	}

	@SuppressWarnings("unused")
	private static Item createSlab(MetalMaterial metal, Block block, BlockSlab slab, BlockSlab doubleslab) {
		final ResourceLocation location = new ResourceLocation(BaseMinerals.MODID, metal.getName() + "_slab");
		final Item item = new ItemMetalSlab(metal, block, slab, doubleslab);
		registerItem(item, location.getResourcePath(), metal, ItemGroups.tab_blocks);
		//item.setUnlocalizedName(location.toString()); // Hack to set name right
		return item;
	}

	@SuppressWarnings("unused")
	private static Item createDoor(MetalMaterial metal, BlockDoor door) {
		final ResourceLocation location = new ResourceLocation(BaseMinerals.MODID, metal.getName() + "_door");
		final Item item = new ItemMetalDoor(door, metal);
		registerItem(item, location.getResourcePath() + "_item", metal, ItemGroups.tab_blocks);
		item.setUnlocalizedName(location.toString()); // Hack to set name right
		doorMap.put(door, item);
		return item;
	}

	/**
	 * 
	 * @param a
	 * @return
	 */
	public static int getSortingValue(ItemStack a) {
		int classVal = 990000;
		int metalVal = 9900;
		if(a.getItem() instanceof ItemBlock && ((ItemBlock)a.getItem()).getBlock() instanceof IMetalObject) {
			classVal = classSortingValues.computeIfAbsent(((ItemBlock)a.getItem()).getBlock().getClass(),
					(Class<?> c)->990000);
			metalVal = materialSortingValues.computeIfAbsent(((IMetalObject)((ItemBlock)a.getItem()).getBlock()).getMetalMaterial(),
					(MetalMaterial m)->9900);
		} else if(a.getItem() instanceof IMetalObject) {
			classVal = classSortingValues.computeIfAbsent(a.getItem().getClass(),
					(Class<?> c)->990000);
			metalVal = materialSortingValues.computeIfAbsent(((IMetalObject)a.getItem()).getMetalMaterial(),
					(MetalMaterial m)->9900);
		}
		return classVal + metalVal + (a.getMetadata() % 100);
	}

	/**
	 * 
	 * @param event
	 */
	@SideOnly(Side.CLIENT)
	public static void registerItemRenders(FMLInitializationEvent event) {
		for(final Item i : itemRegistry.keySet()) {
			Minecraft.getMinecraft().getRenderItem().getItemModelMesher()
			.register(i, 0, 
				new ModelResourceLocation(new ResourceLocation(BaseMinerals.MODID, itemRegistry.get(i)), "inventory"));
		}
	}

	public static Map<Item, String> getItemRegistry() {
		return itemRegistry;
	}
}
