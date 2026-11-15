package zone.moddev.mc.baseminerals.gametest;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;
import zone.moddev.mc.baseminerals.BaseMinerals;

/**
 * In-world checks run by {@code ./gradlew runGameTestServer}, which {@code check} runs.
 * The {@code empty} template is an 8x4x8 air structure in this source set's resources.
 */
@GameTestHolder(BaseMinerals.MOD_ID)
@PrefixGameTestTemplate(false)
public final class BaseMineralsGameTests {
    private static final List<String> MOLTEN = List.of("lithium", "silicon");
    private static final List<String> ORES =
            List.of("lithium", "niter", "phosphorus", "potash", "salt", "saltpeter", "sulfur");
    /** Ores of 1.12's MINERAL materials, which drop powder; lithium was a metal. */
    private static final List<String> DUST_ORES =
            List.of("niter", "phosphorus", "potash", "salt", "saltpeter", "sulfur");

    private BaseMineralsGameTests() {}

    /** Molten minerals burn entities that stand in them, as lava does. */
    @GameTest(template = "empty")
    public static void moltenMineralsSetEntitiesOnFire(GameTestHelper helper) {
        int x = 1;
        Pig[] pigs = new Pig[MOLTEN.size()];
        for (int i = 0; i < MOLTEN.size(); i++, x += 4) {
            BlockPos pos = new BlockPos(x, 1, 3);
            helper.setBlock(pos, block(MOLTEN.get(i)));
            pigs[i] = helper.spawn(EntityType.PIG, pos);
        }
        helper.succeedWhen(() -> {
            for (int i = 0; i < pigs.length; i++) {
                helper.assertTrue(pigs[i].isOnFire(), "Molten " + MOLTEN.get(i) + " did not set the pig on fire");
            }
        });
    }

    /**
     * Breaking an ore in the world: lithium ore drops raw lithium, the other minerals
     * drop their powder. None give experience.
     */
    @GameTest(template = "empty")
    public static void oresDropPowderOrRawLithiumWithoutExperience(GameTestHelper helper) {
        for (int i = 0; i < ORES.size(); i++) {
            BlockPos pos = new BlockPos(i, 1, 3);
            helper.setBlock(pos, block(ORES.get(i) + "_ore"));
            // GameTestHelper.destroyBlock discards drops; break it as the world would.
            helper.getLevel().destroyBlock(helper.absolutePos(pos), true);
        }
        helper.succeedWhen(() -> {
            for (int i = 0; i < ORES.size(); i++) {
                String name = ORES.get(i);
                Item drop = item(DUST_ORES.contains(name) ? name + "_powder" : name + "_raw");
                helper.assertItemEntityPresent(drop, new BlockPos(i, 1, 3), 1.5);
            }
            helper.assertEntityNotPresent(EntityType.EXPERIENCE_ORB);
        });
    }

    /**
     * Mineral ores drop 2 to 4 powders, one more per Fortune level, as 1.12's mineral
     * ores did; Silk Touch keeps the ore. Lithium ore drops one raw lithium, more with
     * Fortune, as vanilla iron ore does.
     */
    @GameTest(template = "empty")
    public static void mineralOreDropsFollowFortuneAndSilkTouch(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        BlockPos pos = helper.absolutePos(new BlockPos(3, 1, 3));
        ItemStack plain = new ItemStack(Items.DIAMOND_PICKAXE);
        ItemStack fortune = enchanted(level, Enchantments.FORTUNE, 3);
        ItemStack silk = enchanted(level, Enchantments.SILK_TOUCH, 1);
        for (String name : ORES) {
            BlockState ore = block(name + "_ore").defaultBlockState();
            Item expected = item(DUST_ORES.contains(name) ? name + "_powder" : name + "_raw");
            int plainMin = Integer.MAX_VALUE;
            int plainMax = 0;
            int fortuneMax = 0;
            for (int roll = 0; roll < 200; roll++) {
                int count = count(helper, Block.getDrops(ore, level, pos, null, null, plain), expected, name);
                plainMin = Math.min(plainMin, count);
                plainMax = Math.max(plainMax, count);
                fortuneMax = Math.max(fortuneMax,
                        count(helper, Block.getDrops(ore, level, pos, null, null, fortune), expected, name));
            }
            List<ItemStack> silkDrops = Block.getDrops(ore, level, pos, null, null, silk);
            helper.assertTrue(silkDrops.size() == 1 && silkDrops.get(0).is(ore.getBlock().asItem())
                    && silkDrops.get(0).getCount() == 1, name + " ore should drop itself with Silk Touch");
            if (DUST_ORES.contains(name)) {
                helper.assertTrue(plainMin == 2 && plainMax == 4,
                        name + " ore dropped " + plainMin + ".." + plainMax + " powder without Fortune");
                helper.assertTrue(fortuneMax > 4 && fortuneMax <= 7,
                        name + " ore dropped at most " + fortuneMax + " powder with Fortune III");
            } else {
                helper.assertTrue(plainMin == 1 && plainMax == 1, name + " ore should drop one raw " + name);
                helper.assertTrue(fortuneMax > 1 && fortuneMax <= 4,
                        name + " ore dropped at most " + fortuneMax + " raw " + name + " with Fortune III");
            }
        }
        helper.succeed();
    }

    private static int count(GameTestHelper helper, List<ItemStack> drops, Item expected, String name) {
        helper.assertTrue(drops.size() == 1 && drops.get(0).is(expected),
                name + " ore dropped " + drops + ", expected " + expected);
        return drops.get(0).getCount();
    }

    private static ItemStack enchanted(ServerLevel level, ResourceKey<Enchantment> key, int enchantmentLevel) {
        ItemStack stack = new ItemStack(Items.DIAMOND_PICKAXE);
        stack.enchant(level.registryAccess().registryOrThrow(Registries.ENCHANTMENT).getHolderOrThrow(key),
                enchantmentLevel);
        return stack;
    }

    /**
     * Mineral blocks and ores sound like stone and resist explosions as 1.13.2's did;
     * molten minerals resist them as lava does.
     */
    @GameTest(template = "empty")
    public static void blocksSoundLikeStoneAndResistExplosions(GameTestHelper helper) {
        BlockPos pos = new BlockPos(3, 1, 3);
        BlockPos absolute = helper.absolutePos(pos);
        Explosion explosion = new Explosion(helper.getLevel(), null, absolute.getX(), absolute.getY(),
                absolute.getZ(), 1.0F, false, Explosion.BlockInteraction.KEEP);
        List<String> solid = new ArrayList<>();
        for (String name : List.of("lithium", "niter", "phosphorus", "potash", "salt", "saltpeter", "silicon",
                "sulfur")) {
            solid.add(name + "_block");
        }
        for (String name : ORES) solid.add(name + "_ore");
        for (String path : solid) {
            helper.setBlock(pos, block(path));
            BlockState state = helper.getBlockState(pos);
            helper.assertTrue(state.getSoundType(helper.getLevel(), absolute, null) == SoundType.STONE,
                    path + " should sound like stone");
            helper.assertTrue(state.getExplosionResistance(helper.getLevel(), absolute, explosion) == 5.0F,
                    path + " should have blast resistance 5");
        }
        for (String name : MOLTEN) {
            helper.setBlock(pos, block(name));
            FluidState fluid = helper.getBlockState(pos).getFluidState();
            helper.assertTrue(fluid.getExplosionResistance(helper.getLevel(), absolute, explosion) == 100.0F,
                    "Molten " + name + " should have blast resistance 100");
        }
        helper.succeed();
    }

    private static Item item(String path) {
        ResourceLocation id = ResourceLocation.fromNamespaceAndPath(BaseMinerals.MOD_ID, path);
        if (!BuiltInRegistries.ITEM.containsKey(id)) throw new IllegalStateException("Missing item " + id);
        return BuiltInRegistries.ITEM.get(id);
    }

    private static Block block(String path) {
        ResourceLocation id = ResourceLocation.fromNamespaceAndPath(BaseMinerals.MOD_ID, path);
        if (!BuiltInRegistries.BLOCK.containsKey(id)) throw new IllegalStateException("Missing block " + id);
        return BuiltInRegistries.BLOCK.get(id);
    }
}
