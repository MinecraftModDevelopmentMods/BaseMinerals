package baseminerals.init;

import baseminerals.BaseMinerals;
//import baseminerals.blocks.*;
import baseminerals.items.*;
import cyano.basemetals.blocks.*;
import cyano.basemetals.items.*;
import cyano.basemetals.material.IMetalObject;
import cyano.basemetals.material.MetalMaterial;
import cyano.basemetals.registry.IOreDictionaryEntry;
import net.minecraft.block.BlockDoor;
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

//	public static Item charcoal_blend;
//	public static Item charcoal_ingot;
	public static Item charcoal_nugget;
	public static Item charcoal_powder;
//	public static Item charcoal_smallblend;
	public static Item charcoal_smallpowder;

//	public static Item lithium_blend;
	public static Item lithium_ingot;
	public static Item lithium_nugget;
	public static Item lithium_powder;
//	public static Item lithium_smallblend;
	public static Item lithium_smallpowder;

//	public static Item niter_blend;
//	public static Item niter_ingot;
//	public static Item niter_nugget;
	public static Item niter_powder;
//	public static Item niter_smallblend;
	public static Item niter_smallpowder;

//	public static Item phosphorus_blend;
//	public static Item phosphorus_ingot;
//	public static Item phosphorus_nugget;
	public static Item phosphorus_powder;
//	public static Item phosphorus_smallblend;
	public static Item phosphorus_smallpowder;

//	public static Item potash_blend;
//	public static Item potash_ingot;
//	public static Item potash_nugget;
	public static Item potash_powder;
//	public static Item potash_smallblend;
	public static Item potash_smallpowder;

//	public static Item salt_blend;
//	public static Item salt_ingot;
//	public static Item salt_nugget;
	public static Item salt_powder;
//	public static Item salt_smallblend;
	public static Item salt_smallpowder;

//	public static Item saltpeter_blend;
//	public static Item saltpeter_ingot;
//	public static Item saltpeter_nugget;
	public static Item saltpeter_powder;
//	public static Item saltpeter_smallblend;
	public static Item saltpeter_smallpowder;

	public static Item silicon_blend;
	public static Item silicon_ingot;
	public static Item silicon_nugget;
	public static Item silicon_powder;
	public static Item silicon_smallblend;
	public static Item silicon_smallpowder;

//	public static Item sulfur_blend;
//	public static Item sulfur_ingot;
//	public static Item sulfur_nugget;
	public static Item sulfur_powder;
//	public static Item sulfur_smallblend;
	public static Item sulfur_smallpowder;

	private static boolean initDone = false;

	private static Map<Item, String> itemRegistry = new HashMap<>();
	private static Map<String, Item> allItems = new HashMap<>();
	private static Map<MetalMaterial, List<Item>> itemsByMetal = new HashMap<>();

	private static Map<BlockDoor,Item> doorMap = new HashMap<>();

	@SuppressWarnings("rawtypes")
	private static Map<Class, Integer> classSortingValues = new HashMap<>();
	private static Map<MetalMaterial, Integer> materialSortingValues = new HashMap<>();

	/**
	 * Gets an item by its name. The name is the name as it is registered in 
	 * the GameRegistry, not its unlocalized name (the unlocalized name is the 
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
		if(initDone) return;
		
		baseminerals.init.Blocks.init();

//		charcoal_blend = create_blend(Materials.charcoal);
//		charcoal_ingot = create_ingot(Materials.charcoal);
		charcoal_nugget = create_nugget(Materials.charcoal); // Special
		charcoal_powder = create_powder(Materials.charcoal);
//		charcoal_smallblend = create_smallblend(Materials.charcoal);
		charcoal_smallpowder = create_smallpowder(Materials.charcoal);

//		lithium_blend = create_blend(Materials.lithium);
		lithium_ingot = create_ingot(Materials.lithium);
		lithium_nugget = create_nugget(Materials.lithium);
		lithium_powder = create_powder(Materials.lithium);
//		lithium_smallblend = create_smallblend(Materials.lithium);
		lithium_smallpowder = create_smallpowder(Materials.lithium);

//		niter_blend = create_blend(Materials.niter);
//		niter_ingot = create_ingot(Materials.niter);
//		niter_nugget = create_nugget(Materials.niter);
		niter_powder = create_powder(Materials.niter);
//		niter_smallblend = create_smallblend(Materials.niter);
		niter_smallpowder = create_smallpowder(Materials.niter);

//		phosphorus_blend = create_blend(Materials.phosphorus);
//		phosphorus_ingot = create_ingot(Materials.phosphorus);
//		phosphorus_nugget = create_nugget(Materials.phosphorus);
		phosphorus_powder = create_powder(Materials.phosphorus);
//		phosphorus_smallblend = create_smallblend(Materials.phosphorus);
		phosphorus_smallpowder = create_smallpowder(Materials.phosphorus);

//		potash_blend = create_blend(Materials.potash);
//		potash_ingot = create_ingot(Materials.potash);
//		potash_nugget = create_nugget(Materials.potash);
		potash_powder = create_powder(Materials.potash);
//		potash_smallblend = create_smallblend(Materials.potash);
		potash_smallpowder = create_smallpowder(Materials.potash);

//		salt_blend = create_blend(Materials.salt);
//		salt_ingot = create_ingot(Materials.salt);
//		salt_nugget = create_nugget(Materials.salt);
		salt_powder = create_powder(Materials.salt);
//		salt_smallblend = create_smallblend(Materials.salt);
		salt_smallpowder = create_smallpowder(Materials.salt);

//		saltpeter_blend = create_blend(Materials.saltpeter);
//		saltpeter_ingot = create_ingot(Materials.saltpeter);
//		saltpeter_nugget = create_nugget(Materials.saltpeter);
		saltpeter_powder = create_powder(Materials.saltpeter);
//		saltpeter_smallblend = create_smallblend(Materials.saltpeter);
		saltpeter_smallpowder = create_smallpowder(Materials.saltpeter);

		silicon_blend = create_blend(Materials.silicon);
		silicon_ingot = create_ingot(Materials.silicon);
		silicon_nugget = create_nugget(Materials.silicon);
		silicon_powder = create_powder(Materials.silicon);
		silicon_smallblend = create_smallblend(Materials.silicon);
		silicon_smallpowder = create_smallpowder(Materials.silicon);

//		sulfur_blend = create_blend(Materials.sulfur);
//		sulfur_ingot = create_ingot(Materials.sulfur);
//		sulfur_nugget = create_nugget(Materials.sulfur);
		sulfur_powder = create_powder(Materials.sulfur);
//		sulfur_smallblend = create_smallblend(Materials.sulfur);
		sulfur_smallpowder = create_smallpowder(Materials.sulfur);

		for(Item i : itemRegistry.keySet()) {
			allItems.put(itemRegistry.get(i), i);
			if(i instanceof IOreDictionaryEntry) { OreDictionary.registerOre(((IOreDictionaryEntry)i).getOreDictionaryName(), i); }
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
		classSortingValues.put(ItemMetalShears.class, ++ss * 10000);
		classSortingValues.put(ItemMetalSmallBlend.class, ++ss * 10000);
		classSortingValues.put(ItemMetalSmallPowder.class, ++ss * 10000);
		classSortingValues.put(ItemMetalDoor.class, classSortingValues.get(BlockMetalDoor.class));

		List<MetalMaterial> metlist = new ArrayList<>(Materials.getAllMetals().size());
		metlist.addAll(Materials.getAllMetals());
		metlist.sort((MetalMaterial a, MetalMaterial b)-> a.getName().compareToIgnoreCase(b.getName()));
		for(int i = 0; i < metlist.size(); i++) {
			materialSortingValues.put(metlist.get(i), i*100);
		}

		initDone = true;
	}

	private static Item registerItem(Item item, String name, MetalMaterial metal, CreativeTabs tab) {
		ResourceLocation location = new ResourceLocation(BaseMinerals.MODID, name);
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

	private static Item create_ingot(MetalMaterial metal) {
		return registerItem(new baseminerals.items.ItemMetalIngot(metal), metal.getName()+"_"+"ingot", metal, ItemGroups.tab_items);
	}

	private static Item create_nugget(MetalMaterial metal) {
		return registerItem(new ItemMetalNugget(metal), metal.getName()+"_"+"nugget", metal, ItemGroups.tab_items);
	}

	private static Item create_powder(MetalMaterial metal) {
		return registerItem(new ItemMetalPowder(metal), metal.getName()+"_"+"powder", metal, ItemGroups.tab_items);
	}

	private static Item create_blend(MetalMaterial metal) {
		return registerItem(new baseminerals.items.ItemMetalBlend(metal), metal.getName()+"_"+"blend", metal, ItemGroups.tab_items);
	}

	private static Item create_rod(MetalMaterial metal) {
		return registerItem(new GenericMetalItem(metal), metal.getName()+"_"+"rod", metal, ItemGroups.tab_items);
	}

	private static Item create_gear(MetalMaterial metal) {
		Item i = registerItem(new GenericMetalItem(metal), metal.getName()+"_"+"gear", metal, ItemGroups.tab_items);
		OreDictionary.registerOre("gear"+metal.getCapitalizedName(), i);
		return i;
	}

	private static Item create_axe(MetalMaterial metal) {
		return registerItem(new ItemMetalAxe(metal), metal.getName()+"_"+"axe", metal, ItemGroups.tab_tools);
	}

	private static Item create_crackhammer(MetalMaterial metal) {
		return registerItem(new ItemMetalCrackHammer(metal), metal.getName()+"_"+"crackhammer", metal, ItemGroups.tab_tools);
	}

	private static Item create_hoe(MetalMaterial metal) {
		return registerItem(new ItemMetalHoe(metal), metal.getName()+"_"+"hoe", metal, ItemGroups.tab_tools);
	}

	private static Item create_pickaxe(MetalMaterial metal) {
		return registerItem(new ItemMetalPickaxe(metal), metal.getName()+"_"+"pickaxe", metal, ItemGroups.tab_tools);
	}

	private static Item create_shovel(MetalMaterial metal) {
		return registerItem(new ItemMetalShovel(metal), metal.getName()+"_"+"shovel", metal, ItemGroups.tab_tools);
	}

	private static Item create_sword(MetalMaterial metal) {
		return registerItem(new ItemMetalSword(metal), metal.getName()+"_"+"sword", metal, ItemGroups.tab_tools);
	}

	private static Item create_helmet(MetalMaterial metal) {
		return registerItem(ItemMetalArmor.createHelmet(metal), metal.getName()+"_"+"helmet", metal, ItemGroups.tab_tools);
	}

	private static Item create_chestplate(MetalMaterial metal) {
		return registerItem(ItemMetalArmor.createChestplate(metal), metal.getName()+"_"+"chestplate", metal, ItemGroups.tab_tools);
	}

	private static Item create_leggings(MetalMaterial metal) {
		return registerItem(ItemMetalArmor.createLeggings(metal), metal.getName()+"_"+"leggings", metal, ItemGroups.tab_tools);
	}

	private static Item create_boots(MetalMaterial metal) {
		return registerItem(ItemMetalArmor.createBoots(metal), metal.getName()+"_"+"boots", metal, ItemGroups.tab_tools);
	}

	private static Item create_horsearmor(MetalMaterial metal) {
		return registerItem(new ItemMetalHorseArmor(metal), metal.getName()+"_"+"horsearmor", metal, ItemGroups.tab_tools);
	}

	private static Item create_arrow(MetalMaterial metal) {
		return registerItem(new ItemMetalArrow(metal), metal.getName()+"_"+"arrow", metal, ItemGroups.tab_tools);
	}

	private static Item create_bolt(MetalMaterial metal) {
		return registerItem(new ItemMetalBolt(metal), metal.getName()+"_"+"bolt", metal, ItemGroups.tab_tools);
	}

	private static Item create_bow(MetalMaterial metal) {
		return registerItem(new ItemMetalBow(metal), metal.getName()+"_"+"bow", metal, ItemGroups.tab_tools);
	}

	private static Item create_crossbow(MetalMaterial metal) {
		return registerItem(new ItemMetalCrossbow(metal), metal.getName()+"_"+"crossbow", metal, ItemGroups.tab_tools);
	}

	private static Item create_shears(MetalMaterial metal) {
		return registerItem(new ItemMetalShears(metal), metal.getName()+"_"+"shears", metal, ItemGroups.tab_tools);
	}

	private static Item create_smallblend(MetalMaterial metal) {
		return registerItem(new ItemMetalSmallBlend(metal), metal.getName()+"_"+"smallblend", metal, ItemGroups.tab_items);
	}

	private static Item create_fishingrod(MetalMaterial metal) {
		return registerItem(new ItemMetalFishingRod(metal), metal.getName()+"_"+"fishingrod", metal, ItemGroups.tab_tools);
	}

	private static Item create_smallpowder(MetalMaterial metal) {
		return registerItem(new ItemMetalSmallPowder(metal), metal.getName()+"_"+"smallpowder", metal, ItemGroups.tab_items);
	}

	private static Item create_door(MetalMaterial metal, BlockDoor door) {
		ResourceLocation location = new ResourceLocation(BaseMinerals.MODID, metal.getName()+"_"+"door");
		Item item = new ItemMetalDoor(door, metal);
		registerItem(item, location.getResourcePath()+"_"+"item", metal, ItemGroups.tab_blocks);
		item.setUnlocalizedName(location.toString()); // Hack to set name right
		doorMap.put(door, item);
		return item;
	}

	/**
	 * 
	 * @param a
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static int getSortingValue(ItemStack a) {
		int classVal = 990000;
		int metalVal = 9900;
		if(a.getItem() instanceof ItemBlock && ((ItemBlock)a.getItem()).getBlock() instanceof IMetalObject) {
			classVal = classSortingValues.computeIfAbsent(((ItemBlock)a.getItem()).getBlock().getClass(),
					(Class c)->990000);
			metalVal = materialSortingValues.computeIfAbsent(((IMetalObject)((ItemBlock)a.getItem()).getBlock()).getMetalMaterial(),
					(MetalMaterial m)->9900);
		} else if(a.getItem() instanceof IMetalObject) {
			classVal = classSortingValues.computeIfAbsent(a.getItem().getClass(),
					(Class c)->990000);
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
		for(Item i : itemRegistry.keySet()) {
			Minecraft.getMinecraft().getRenderItem().getItemModelMesher()
			.register(i, 0, 
				new ModelResourceLocation(new ResourceLocation(BaseMinerals.MODID, itemRegistry.get(i)), "inventory"));
		}
	}
}
