package zone.moddev.mc.baseminerals.loot;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootFunction;
import net.minecraft.world.storage.loot.LootParameters;
import net.minecraft.world.storage.loot.conditions.ILootCondition;

/** Preserves the earlier uniform 2..4+Fortune powder yield in native block loot. */
public final class MineralPowderCount extends LootFunction {
    private MineralPowderCount(ILootCondition[] conditions) { super(conditions); }

    @Override protected ItemStack doApply(ItemStack stack, LootContext context) {
        ItemStack tool = context.get(LootParameters.TOOL);
        int fortune = tool == null ? 0 : EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, tool);
        stack.setCount(2 + context.getRandom().nextInt(3 + Math.max(0, fortune)));
        return stack;
    }

    public static final class Serializer extends LootFunction.Serializer<MineralPowderCount> {
        public Serializer() { super(new ResourceLocation("baseminerals", "mineral_powder_count"), MineralPowderCount.class); }
        @Override public MineralPowderCount deserialize(JsonObject json, JsonDeserializationContext context,
                ILootCondition[] conditions) { return new MineralPowderCount(conditions); }
    }
}
