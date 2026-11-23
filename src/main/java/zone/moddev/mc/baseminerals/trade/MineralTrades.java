package zone.moddev.mc.baseminerals.trade;

import net.minecraft.entity.merchant.villager.VillagerProfession;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.MerchantOffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import zone.moddev.mc.baseminerals.BaseMinerals;

/** Carries the level-two ingot offers to the three native smith professions. */
@Mod.EventBusSubscriber(modid = BaseMinerals.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class MineralTrades {
    private MineralTrades() {}
    @SubscribeEvent
    public static void register(VillagerTradesEvent event) {
        VillagerProfession type = event.getType();
        if (type != VillagerProfession.ARMORER && type != VillagerProfession.WEAPONSMITH
                && type != VillagerProfession.TOOLSMITH) return;
        for (String material : new String[] {"lithium", "silicon"}) {
            Item ingot = ForgeRegistries.ITEMS.getValue(new ResourceLocation("baseminerals", material + "_ingot"));
            event.getTrades().get(2).add((merchant, random) -> new MerchantOffer(
                    new ItemStack(Items.EMERALD, 4 + random.nextInt(2)), new ItemStack(ingot), 12, 5, 0.05F));
        }
    }
}
