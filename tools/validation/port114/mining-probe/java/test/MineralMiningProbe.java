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
import net.minecraft.block.BlockState;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipe;
import net.minecraft.item.crafting.AbstractCookingRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.inventory.Inventory;
import net.minecraft.network.PacketDirection;
import net.minecraft.network.play.ServerPlayNetHandler;
import net.minecraft.network.NetworkManager;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.GameType;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.item.MerchantOffer;
import net.minecraft.item.MerchantOffers;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.event.server.FMLServerStartedEvent;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraft.entity.merchant.villager.VillagerProfession;
import net.minecraft.entity.merchant.villager.VillagerTrades;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.management.PlayerInteractionManager;
import net.minecraft.world.storage.loot.*;
import net.minecraft.world.storage.loot.conditions.ILootCondition;
import zone.moddev.mc.baseminerals.loot.MineralPowderCount;

/** Exercises the installed jar through real survival harvesting. */
@Mod("mineralminingprobe")
public final class MineralMiningProbe {
    private static final String[] MINERALS = {
        "lithium", "niter", "phosphorus", "potash", "salt", "saltpeter", "sulfur"
    };
    private final List<String> failures = new ArrayList<>();
    private final JsonObject drops = new JsonObject();
    private final JsonObject miningProgress = new JsonObject();
    private ServerWorld world;
    private ServerPlayerEntity player;
    private int checks;
    private int harvestIndex;

    public MineralMiningProbe() { MinecraftForge.EVENT_BUS.addListener(this::started); }

    private void started(FMLServerStartedEvent event) {
        world = event.getServer().getWorld(DimensionType.OVERWORLD);
        player = new ServerPlayerEntity(event.getServer(), world, new GameProfile(UUID.randomUUID(), "MineralMiningProbe"), new PlayerInteractionManager(world));
        player.connection = new ServerPlayNetHandler(event.getServer(), new NetworkManager(PacketDirection.SERVERBOUND), player);
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
                    BlockState state = ore.getDefaultState();
                    for (int sample = 0; sample < 256; sample++) {
                        Random random = new Random(sample);
                        int expected = lithium ? 1 : 2 + new Random(sample).nextInt(3 + fortune);
                        LootContext.Builder context = new LootContext.Builder(world).withRandom(random)
                                .withParameter(LootParameters.POSITION, BlockPos.ZERO)
                                .withParameter(LootParameters.BLOCK_STATE, state).withParameter(LootParameters.TOOL, pickaxe);
                        int actual = lithium ? 1 : new MineralPowderCount.Serializer()
                                .deserialize(new JsonObject(), null, new ILootCondition[0])
                                .apply(new ItemStack(item(mineral + "_powder")), context.build(LootParameterSets.BLOCK)).getCount();
                        check(actual == expected, mineral + " changed the uniform Fortune formula at " + fortune);
                        List<ItemStack> loot = ore.getDrops(state, new LootContext.Builder(world).withRandom(new Random(sample))
                                .withParameter(LootParameters.POSITION, BlockPos.ZERO).withParameter(LootParameters.TOOL, pickaxe));
                        check(loot.size() == 1 && loot.get(0).getItem() == item(mineral + (lithium ? "_ore" : "_powder"))
                                        && (lithium ? loot.get(0).getCount() == 1 : loot.get(0).getCount() >= 2 && loot.get(0).getCount() <= 4 + fortune),
                                mineral + " native loot changed at Fortune " + fortune);
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
                check(ore.getExpDrop(ore.getDefaultState(), world, BlockPos.ZERO, 3, 0) == 0,
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
                    moltenLifecycle(material);
                }
            }
            for (String owner : new String[] {"basemetals", "modernmetals"}) {
                if (!ModList.get().isLoaded(owner)) continue;
                ResourceLocation hammerId = new ResourceLocation(owner,
                        "basemetals".equals(owner) ? "iron_crackhammer" : "galvanizedsteel_crackhammer");
                check(ForgeRegistries.ITEMS.containsKey(hammerId), "Missing test hammer " + hammerId);
                for (String mineral : MINERALS) {
                    ItemStack hammer = new ItemStack(ForgeRegistries.ITEMS.getValue(hammerId));
                    int count = dig(block(mineral + "_ore"), hammer, item(mineral + "_powder"));
                    check(count == 2 && hammer.getDamage() == 1,
                            owner + " hammer yielded " + count + " " + mineral + " powders, damage " + hammer.getDamage());
                }
                if (Boolean.getBoolean("mineralprobe.processing")) {
                    for (String mineral : new String[] {"lithium", "niter", "phosphorus", "potash", "salt",
                            "saltpeter", "silicon", "sulfur"}) {
                        ItemStack hammer = new ItemStack(ForgeRegistries.ITEMS.getValue(hammerId));
                        check(dig(block(mineral + "_block"), hammer, item(mineral + "_powder")) == 9,
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
            report.add("mining_progress", miningProgress);
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
                event.getServer().initiateShutdown(false);
            }
        }
    }

    private void blockProperties(Block block, float hardness, float resistance, int harvestLevel, boolean beacon) {
        BlockState state = block.getDefaultState();
        check(state.getBlockHardness(world, BlockPos.ZERO) == hardness,
                block.getRegistryName() + " changed mining hardness");
        check(block.getExplosionResistance() == resistance, block.getRegistryName() + " changed blast resistance");
        check(state.getHarvestLevel() == harvestLevel, block.getRegistryName() + " changed harvest tier");
        check(block.isBeaconBase(state, world, BlockPos.ZERO, BlockPos.ZERO) == beacon,
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
        for (VillagerProfession profession : new VillagerProfession[] {VillagerProfession.ARMORER,
                VillagerProfession.TOOLSMITH, VillagerProfession.WEAPONSMITH}) {
            for (int seed = 0; seed < 32; seed++) {
                MerchantOffers offers = new MerchantOffers();
                for (VillagerTrades.ITrade trade : VillagerTrades.field_221239_a.get(profession).get(2)) {
                    if (trade.getClass().getName().startsWith("zone.moddev.mc.baseminerals.trade."))
                        offers.add(trade.getOffer(null, new Random(seed)));
                }
                for (String material : new String[] {"lithium", "silicon"}) {
                    int matches = 0;
                    for (MerchantOffer offer : offers) {
                        if (offer.func_222206_f().getItem() != item(material + "_ingot")) continue;
                        matches++;
                        check(offer.func_222206_f().getCount() == 1 && offer.func_222218_a().getItem() == Items.EMERALD
                                        && offer.func_222218_a().getCount() >= 4 && offer.func_222218_a().getCount() <= 5
                                        && offer.func_222214_i() == 12 && offer.func_222210_n() == 5,
                                material + " smith offer, restocking or experience changed");
                    }
                    check(matches == 1, profession + " has " + matches + " " + material + " offers");
                }
            }
        }
    }

    private void furnace(String input, String output, float xp) {
        cooking(input, output, xp, IRecipeType.SMELTING, 200);
        cooking(input, output, xp, IRecipeType.BLASTING, 100);
    }

    private void cooking(String input, String output, float xp, IRecipeType<?> type, int ticks) {
        Inventory inventory = new Inventory(1);
        inventory.setInventorySlotContents(0, new ItemStack(item(input)));
        List<IRecipe> matches = new ArrayList<>();
        for (IRecipe recipe : world.getRecipeManager().getRecipes()) {
            if (recipe.getType() == type && recipe.matches(inventory, world)) matches.add(recipe);
        }
        check(matches.size() == 1, input + " has " + matches.size() + " furnace recipes");
        for (IRecipe recipe : matches) {
            ItemStack result = recipe.getRecipeOutput();
            check(result.getItem() == item(output) && result.getCount() == 1, input + " smelts to " + result);
            check(((AbstractCookingRecipe) recipe).getExperience() == xp, input + " changed cooking XP");
            check(((AbstractCookingRecipe) recipe).getCookTime() == ticks, input + " changed cooking time");
        }
    }

    private void crusher(String input, String output, int count, boolean enabled) {
        Inventory inventory = new Inventory(1);
        inventory.setInventorySlotContents(0, new ItemStack(item(input)));
        List<IRecipe> matches = new ArrayList<>();
        for (IRecipe recipe : world.getRecipeManager().getRecipes()) {
            if ("crushing".equals(recipe.getSerializer().getRegistryName().getPath()) && recipe.matches(inventory, world)) {
                matches.add(recipe);
            }
        }
        check(matches.size() == (enabled ? 1 : 0), input + " has " + matches.size() + " crushing recipes");
        for (IRecipe recipe : matches) {
            check(recipe.getRecipeOutput().getItem() == item(output) && recipe.getRecipeOutput().getCount() == count,
                    input + " crushes to " + recipe.getRecipeOutput());
        }
    }

    private void moltenLifecycle(String material) {
        net.minecraft.item.BucketItem bucket = (net.minecraft.item.BucketItem) item(material + "_bucket");
        net.minecraft.fluid.Fluid fluid = bucket.getFluid();
        net.minecraftforge.fluids.FluidAttributes attributes = fluid.getAttributes();
        check(fluid.getRegistryName().equals(new ResourceLocation("baseminerals", material)), "Source fluid identity " + material);
        check(attributes.getColor() == 0xFFC5C8C1 && attributes.getLuminosity() == 10
                && attributes.getDensity() == 2000 && attributes.getViscosity() == 10000
                && attributes.getTemperature() == 330, "Molten attributes " + material);
        check(!fluid.isIn(net.minecraft.tags.FluidTags.LAVA), "Molten fluid became vanilla lava " + material);
        BlockPos pos = new BlockPos(128, 240, "lithium".equals(material) ? 144 : 160);
        for (int x = -5; x <= 5; x++) for (int z = -5; z <= 5; z++) {
            world.setBlockState(pos.add(x, -1, z), net.minecraft.block.Blocks.STONE.getDefaultState(), 2);
            world.setBlockState(pos.add(x, 0, z), net.minecraft.block.Blocks.AIR.getDefaultState(), 2);
        }
        check(bucket.tryPlaceContainedLiquid(player, world, pos, null), "Native bucket placement " + material);
        check(world.getBlockState(pos).getBlock() == block(material) && world.getFluidState(pos).isSource(),
                "Bucket placed the wrong source " + material);
        world.getFluidState(pos).tick(world, pos);
        boolean flowed = false;
        for (net.minecraft.util.Direction side : net.minecraft.util.Direction.Plane.HORIZONTAL) {
            net.minecraft.fluid.IFluidState adjacent = world.getFluidState(pos.offset(side));
            if (fluid.isEquivalentTo(adjacent.getFluid()) && !adjacent.isSource()) flowed = true;
        }
        check(flowed, "Molten source did not flow " + material);
        // A newly constructed ServerPlayerEntity has 60 ticks of native spawn
        // damage immunity. Use an ordinary creature to exercise actual contact.
        net.minecraft.entity.passive.PigEntity creature = net.minecraft.entity.EntityType.PIG.create(world);
        creature.setPosition(pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5);
        float health = creature.getHealth();
        block(material).onEntityCollision(world.getBlockState(pos), world, pos, creature);
        check(creature.getHealth() == health && !creature.isBurning(), "Molten heat escaped its surface " + material);
        creature.setPosition(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
        block(material).onEntityCollision(world.getBlockState(pos), world, pos, creature);
        check(creature.getHealth() == health - 4.0F && creature.isBurning(),
                "Molten contact lost heat " + material + " health=" + creature.getHealth() + " burning=" + creature.isBurning());
        creature.remove();
        net.minecraft.fluid.Fluid picked = ((net.minecraft.block.IBucketPickupHandler) block(material))
                .pickupFluid(world, pos, world.getBlockState(pos));
        check(picked == fluid && world.isAirBlock(pos) && picked.getFilledBucket() == bucket,
                "Native pickup lost fluid or bucket identity " + material);
    }

    private int harvest(Block block, ItemStack tool, Item expected) {
        return harvest(block, tool, expected, false);
    }

    private int dig(Block block, ItemStack tool, Item expected) {
        return harvest(block, tool, expected, true);
    }

    private int harvest(Block block, ItemStack tool, Item expected, boolean normalDigging) {
        int index = harvestIndex++;
        BlockPos pos = new BlockPos(128 + (index % 16) * 3, 180 + (index / 16) * 4, 128);
        AxisAlignedBB area = new AxisAlignedBB(pos).grow(0.45);
        world.getEntitiesWithinAABB(ItemEntity.class, area).forEach(ItemEntity::remove);
        world.setBlockState(pos, block.getDefaultState(), 3);
        player.setHeldItem(Hand.MAIN_HAND, tool);
        if (normalDigging) {
            player.setPosition(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 2.5D);
            player.onGround = true;
            BlockState state = world.getBlockState(pos);
            float speed = tool.getDestroySpeed(state);
            check(speed > 1.0F, "Hammer uses untagged mining speed for " + block.getRegistryName());
            float progress = state.getPlayerRelativeBlockHardness(player, world, pos);
            int ticks = progress > 0 ? (int) Math.ceil(1.0F / progress) : 400;
            int expectedTicks = (int) Math.ceil(state.getBlockHardness(world, pos) * 30.0F / speed);
            check(state.canHarvestBlock(world, pos, player) && ticks <= expectedTicks + 1,
                    "Hammer progress differs from its native speed for " + block.getRegistryName() + ": " + ticks + " vs " + expectedTicks);
            JsonObject timing = new JsonObject();
            timing.addProperty("speed", speed); timing.addProperty("ticks", ticks);
            timing.addProperty("progress_per_tick", progress);
            miningProgress.add(tool.getItem().getRegistryName() + "/" + block.getRegistryName(), timing);
            player.interactionManager.func_225416_a(pos, net.minecraft.network.play.client.CPlayerDiggingPacket.Action.START_DESTROY_BLOCK,
                    net.minecraft.util.Direction.UP, 256);
            for (int tick = 0; tick <= Math.min(ticks, 400); tick++) player.interactionManager.tick();
            player.interactionManager.func_225416_a(pos, net.minecraft.network.play.client.CPlayerDiggingPacket.Action.STOP_DESTROY_BLOCK,
                    net.minecraft.util.Direction.UP, 256);
            check(world.isAirBlock(pos), "Normal digging did not remove " + block.getRegistryName());
        } else player.interactionManager.tryHarvestBlock(pos);
        int count = 0;
        for (ItemEntity drop : world.getEntitiesWithinAABB(ItemEntity.class, area)) {
            check(drop.getItem().getItem() == expected,
                    block.getRegistryName() + " dropped " + drop.getItem().getItem().getRegistryName());
            count += drop.getItem().getCount();
            drop.remove();
        }
        world.removeBlock(pos, false);
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
