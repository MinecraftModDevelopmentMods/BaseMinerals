package test;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.mojang.authlib.GameProfile;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipe;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.network.EnumPacketDirection;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.network.NetworkManager;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.GameType;
import net.minecraft.world.WorldServer;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.event.server.FMLServerStartedEvent;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.fml.common.registry.VillagerRegistry.VillagerProfession;

/** Exercises the installed jar through real survival harvesting. */
@Mod("mineralminingprobe")
public final class MineralMiningProbe {
    private static final String[] MINERALS = {
        "lithium", "niter", "phosphorus", "potash", "salt", "saltpeter", "sulfur"
    };
    private final List<String> failures = new ArrayList<>();
    private final JsonObject drops = new JsonObject();
    private WorldServer world;
    private FakePlayer player;
    private int checks;
    private int harvestIndex;

    public MineralMiningProbe() { MinecraftForge.EVENT_BUS.addListener(this::started); }

    private void started(FMLServerStartedEvent event) {
        world = event.getServer().getWorld(DimensionType.OVERWORLD);
        player = new FakePlayer(world, new GameProfile(UUID.randomUUID(), "MineralMiningProbe"));
        new NetHandlerPlayServer(event.getServer(), new NetworkManager(EnumPacketDirection.SERVERBOUND), player);
        player.interactionManager.setGameType(GameType.SURVIVAL);
        player.onGround = true;
        try {
            for (String mineral : MINERALS) {
                Block ore = block(mineral + "_ore");
                boolean lithium = "lithium".equals(mineral);
                for (int fortune = 0; fortune <= 3; fortune++) {
                    ItemStack pickaxe = new ItemStack(Items.DIAMOND_PICKAXE);
                    if (fortune > 0) pickaxe.addEnchantment(Enchantments.FORTUNE, fortune);
                    int count = harvest(ore, pickaxe, item(mineral + (lithium ? "_ore" : "_powder")));
                    check(lithium ? count == 1 : count >= 2 && count <= 4 + fortune,
                            mineral + " Fortune " + fortune + " yielded " + count);
                    drops.addProperty(mineral + ":fortune" + fortune, count);
                    IBlockState state = ore.getDefaultState();
                    for (int sample = 0; sample < 256; sample++) {
                        Random random = new Random(sample);
                        int expected = lithium ? 1 : 2 + new Random(sample).nextInt(3 + fortune);
                        check(ore.getItemsToDropCount(state, fortune, world, BlockPos.ORIGIN, random) == expected,
                                mineral + " has different Fortune distribution at " + fortune);
                    }
                }
                ItemStack silk = new ItemStack(Items.DIAMOND_PICKAXE);
                silk.addEnchantment(Enchantments.SILK_TOUCH, 1);
                check(harvest(ore, silk, item(mineral + "_ore")) == 1,
                        mineral + " Silk Touch did not return one ore");
                check(harvest(ore, new ItemStack(Items.WOODEN_PICKAXE), null) == 0,
                        mineral + " dropped with an inadequate pickaxe");
                if (lithium) {
                    check(harvest(ore, new ItemStack(Items.STONE_PICKAXE), null) == 0,
                            "Lithium requires an iron-level pickaxe");
                }
                check(ore.getExpDrop(ore.getDefaultState(), world, BlockPos.ORIGIN, 3) == 0,
                        mineral + " added mining XP");
                blockProperties(ore, 5.0F, lithium ? 3.0F : 0.75F, lithium ? 2 : 1, false);
            }
            for (String mineral : new String[] {"lithium", "niter", "phosphorus", "potash", "salt",
                    "saltpeter", "silicon", "sulfur"}) {
                Block storage = block(mineral + "_block");
                check(harvest(storage, new ItemStack(Items.DIAMOND_PICKAXE), item(mineral + "_block")) == 1,
                        mineral + " storage-block drop changed");
                boolean metal = "lithium".equals(mineral) || "silicon".equals(mineral);
                blockProperties(storage, metal ? 16.0F : 10.0F, metal ? 4.0F : 1.0F, metal ? 2 : 1, true);
            }
            if (Boolean.getBoolean("mineralprobe.processing")) {
                processingRecipes();
                smithOffers();
                for (String material : new String[] {"lithium", "silicon"}) {
                    check(block(material).getDefaultState().getLightValue() == 10, material + " lost molten-fluid light");
                }
            }
            for (String owner : new String[] {"basemetals", "modernmetals"}) {
                if (!ModList.get().isLoaded(owner)) continue;
                ResourceLocation hammerId = new ResourceLocation(owner,
                        "basemetals".equals(owner) ? "iron_crackhammer" : "galvanizedsteel_crackhammer");
                check(ForgeRegistries.ITEMS.containsKey(hammerId), "Missing test hammer " + hammerId);
                for (String mineral : MINERALS) {
                    ItemStack hammer = new ItemStack(ForgeRegistries.ITEMS.getValue(hammerId));
                    int count = harvest(block(mineral + "_ore"), hammer, item(mineral + "_powder"));
                    check(count == 2 && hammer.getDamage() == 1,
                            owner + " hammer yielded " + count + " " + mineral + " powders, damage " + hammer.getDamage());
                }
                if (Boolean.getBoolean("mineralprobe.processing")) {
                    for (String mineral : new String[] {"lithium", "niter", "phosphorus", "potash", "salt",
                            "saltpeter", "silicon", "sulfur"}) {
                        ItemStack hammer = new ItemStack(ForgeRegistries.ITEMS.getValue(hammerId));
                        check(harvest(block(mineral + "_block"), hammer, item(mineral + "_powder")) == 9,
                                owner + " hammer did not crush " + mineral + " storage block to nine powders");
                        check(hammer.getDamage() == 1, owner + " storage-block crushing did not damage hammer");
                    }
                }
            }
        } catch (Throwable error) {
            failures.add(error.toString());
            error.printStackTrace();
        } finally {
            JsonObject report = new JsonObject();
            report.addProperty("passed", failures.isEmpty());
            report.addProperty("checks", checks);
            report.add("drops", drops);
            report.add("failures", new com.google.gson.Gson().toJsonTree(failures));
            try {
                Files.write(Paths.get("mining-results.json"),
                        new GsonBuilder().setPrettyPrinting().create().toJson(report).getBytes(StandardCharsets.UTF_8));
            } catch (java.io.IOException error) {
                throw new IllegalStateException(error);
            } finally {
                System.out.println("MINERAL_MINING_PROBE " + (failures.isEmpty() ? "PASS" : "FAIL") + " checks=" + checks);
                event.getServer().initiateShutdown();
            }
        }
    }

    private void blockProperties(Block block, float hardness, float resistance, int harvestLevel, boolean beacon) {
        IBlockState state = block.getDefaultState();
        check(state.getBlockHardness(world, BlockPos.ORIGIN) == hardness,
                block.getRegistryName() + " changed mining hardness");
        check(block.getExplosionResistance() == resistance, block.getRegistryName() + " changed blast resistance");
        check(state.getHarvestLevel() == harvestLevel, block.getRegistryName() + " changed harvest tier");
        check(block.isBeaconBase(state, world, BlockPos.ORIGIN, BlockPos.ORIGIN) == beacon,
                block.getRegistryName() + " changed beacon support");
    }

    private void processingRecipes() {
        for (String material : new String[] {"lithium", "silicon"}) {
            furnace(material + "_powder", material + "_ingot", 0.45F);
            furnace(material + "_smallpowder", material + "_nugget", 0.05F);
        }
        furnace("lithium_ore", "lithium_ingot", 0.45F);
        furnace("silicon_blend", "silicon_ingot", 0.45F);
        furnace("silicon_smallblend", "silicon_nugget", 0.05F);
        boolean crushingLoaded = ModList.get().isLoaded("basemetals") || ModList.get().isLoaded("modernmetals");
        for (String material : new String[] {"lithium", "niter", "phosphorus", "potash", "salt",
                "saltpeter", "silicon", "sulfur"}) {
            crusher(material + "_block", material + "_powder", 9, crushingLoaded);
        }
        for (String material : new String[] {"lithium", "silicon"}) {
            crusher(material + "_ingot", material + "_powder", 1, crushingLoaded);
            crusher(material + "_nugget", material + "_smallpowder", 1, crushingLoaded);
        }
    }

    private void smithOffers() {
        VillagerProfession smith = ForgeRegistries.VILLAGER_PROFESSIONS.getValue(new ResourceLocation("minecraft:smith"));
        EntityVillager merchant = new EntityVillager(world);
        for (int career = 1; career <= 3; career++) {
            for (int seed = 0; seed < 32; seed++) {
                MerchantRecipeList offers = new MerchantRecipeList();
                for (EntityVillager.ITradeList trade : smith.getCareer(career).getTrades(1)) {
                    trade.addMerchantRecipe(merchant, offers, new Random(seed));
                }
                for (String material : new String[] {"lithium", "silicon"}) {
                    int matches = 0;
                    for (MerchantRecipe offer : offers) {
                        if (offer.getItemToSell().getItem() != item(material + "_ingot")) continue;
                        matches++;
                        check(offer.getItemToSell().getCount() == 1 && offer.getItemToBuy().getItem() == Items.EMERALD
                                        && offer.getItemToBuy().getCount() >= 4 && offer.getItemToBuy().getCount() <= 5,
                                material + " smith offer changed");
                    }
                    check(matches == 1, "Career " + career + " has " + matches + " " + material + " offers");
                }
            }
        }
    }

    private void furnace(String input, String output, float xp) {
        InventoryBasic inventory = new InventoryBasic(new TextComponentString("smelting"), 1);
        inventory.setInventorySlotContents(0, new ItemStack(item(input)));
        List<IRecipe> matches = new ArrayList<>();
        for (IRecipe recipe : world.getRecipeManager().getRecipes()) {
            if (recipe instanceof FurnaceRecipe && recipe.matches(inventory, world)) matches.add(recipe);
        }
        check(matches.size() == 1, input + " has " + matches.size() + " furnace recipes");
        for (IRecipe recipe : matches) {
            ItemStack result = recipe.getRecipeOutput();
            check(result.getItem() == item(output) && result.getCount() == 1, input + " smelts to " + result);
            check(((FurnaceRecipe) recipe).getExperience() == xp, input + " changed smelting XP");
        }
    }

    private void crusher(String input, String output, int count, boolean enabled) {
        InventoryBasic inventory = new InventoryBasic(new TextComponentString("crushing"), 1);
        inventory.setInventorySlotContents(0, new ItemStack(item(input)));
        List<IRecipe> matches = new ArrayList<>();
        for (IRecipe recipe : world.getRecipeManager().getRecipes()) {
            if ("crushing".equals(recipe.getSerializer().getName().getPath()) && recipe.matches(inventory, world)) {
                matches.add(recipe);
            }
        }
        check(matches.size() == (enabled ? 1 : 0), input + " has " + matches.size() + " crushing recipes");
        for (IRecipe recipe : matches) {
            check(recipe.getRecipeOutput().getItem() == item(output) && recipe.getRecipeOutput().getCount() == count,
                    input + " crushes to " + recipe.getRecipeOutput());
        }
    }

    private int harvest(Block block, ItemStack tool, Item expected) {
        int index = harvestIndex++;
        BlockPos pos = new BlockPos(128 + (index % 16) * 3, 180 + (index / 16) * 4, 128);
        AxisAlignedBB area = new AxisAlignedBB(pos).grow(0.45);
        world.getEntitiesWithinAABB(EntityItem.class, area).forEach(EntityItem::remove);
        world.setBlockState(pos, block.getDefaultState(), 3);
        player.setHeldItem(EnumHand.MAIN_HAND, tool);
        player.interactionManager.tryHarvestBlock(pos);
        int count = 0;
        for (EntityItem drop : world.getEntitiesWithinAABB(EntityItem.class, area)) {
            check(drop.getItem().getItem() == expected,
                    block.getRegistryName() + " dropped " + drop.getItem().getItem().getRegistryName());
            count += drop.getItem().getCount();
            drop.remove();
        }
        world.removeBlock(pos);
        return count;
    }

    private static Block block(String name) {
        return ForgeRegistries.BLOCKS.getValue(new ResourceLocation("baseminerals", name));
    }

    private static Item item(String name) {
        return ForgeRegistries.ITEMS.getValue(new ResourceLocation("baseminerals", name));
    }

    private void check(boolean condition, String detail) {
        checks++;
        if (!condition) failures.add(detail);
    }
}
