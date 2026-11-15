package zone.moddev.mc.baseminerals.trade;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.village.MerchantRecipe;
import net.minecraftforge.fml.common.registry.VillagerRegistry.VillagerProfession;
import net.minecraftforge.registries.ForgeRegistries;

/** The old shared library offered one ingot for four or five emeralds at smith level two. */
public final class MineralTrades {
    private MineralTrades() {}

    public static void register() {
        VillagerProfession smith = ForgeRegistries.VILLAGER_PROFESSIONS.getValue(
                new ResourceLocation("minecraft", "smith"));
        for (int career = 1; career <= 3; career++) {
            for (String material : new String[] {"lithium", "silicon"}) {
                Item ingot = ForgeRegistries.ITEMS.getValue(new ResourceLocation("baseminerals", material + "_ingot"));
                smith.getCareer(career).addTrade(2, (merchant, recipes, random) -> recipes.add(
                        new MerchantRecipe(new ItemStack(Items.EMERALD, 4 + random.nextInt(2)), new ItemStack(ingot))));
            }
        }
    }
}
