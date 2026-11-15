package zone.moddev.mc.baseminerals.gametest;

import java.util.List;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.level.block.Block;
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

    /** Ores drop themselves through their loot tables and give no experience. */
    @GameTest(template = "empty")
    public static void oresDropThemselvesWithoutExperience(GameTestHelper helper) {
        for (int i = 0; i < ORES.size(); i++) {
            BlockPos pos = new BlockPos(i, 1, 3);
            helper.setBlock(pos, block(ORES.get(i) + "_ore"));
            // GameTestHelper.destroyBlock discards drops; break it as the world would.
            helper.getLevel().destroyBlock(helper.absolutePos(pos), true);
        }
        helper.succeedWhen(() -> {
            for (int i = 0; i < ORES.size(); i++) {
                helper.assertItemEntityPresent(block(ORES.get(i) + "_ore").asItem(), new BlockPos(i, 1, 3), 1.5);
            }
            helper.assertEntityNotPresent(EntityType.EXPERIENCE_ORB);
        });
    }

    private static Block block(String path) {
        ResourceLocation id = ResourceLocation.fromNamespaceAndPath(BaseMinerals.MOD_ID, path);
        if (!BuiltInRegistries.BLOCK.containsKey(id)) throw new IllegalStateException("Missing block " + id);
        return BuiltInRegistries.BLOCK.get(id);
    }
}
