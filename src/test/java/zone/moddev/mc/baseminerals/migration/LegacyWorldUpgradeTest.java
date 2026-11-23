package zone.moddev.mc.baseminerals.migration;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class LegacyWorldUpgradeTest {
    @Test
    void convertsNestedMineralBucketsAndKeepsTheRestOfTheStack() {
        CompoundNBT lithium = bucket("lithium");
        CompoundNBT silicon = bucket("baseminerals:silicon");
        ListNBT inventory = new ListNBT();
        inventory.add(lithium);
        inventory.add(silicon);
        CompoundNBT player = new CompoundNBT();
        player.put("Inventory", inventory);

        LegacyWorldUpgrade.migrateItems(player);
        CompoundNBT afterFirstPass = player.copy();
        LegacyWorldUpgrade.migrateItems(player);

        assertEquals(afterFirstPass, player);
        assertEquals("baseminerals:lithium_bucket", lithium.getString("id"));
        assertEquals("baseminerals:silicon_bucket", silicon.getString("id"));
        for (CompoundNBT stack : new CompoundNBT[] {lithium, silicon}) {
            assertEquals(1, stack.getByte("Count"));
            assertEquals("Keep me", stack.getCompound("tag").getString("Custom"));
            assertFalse(stack.getCompound("tag").contains("FluidName", 8));
            assertFalse(stack.getCompound("tag").contains("Amount", 99));
        }
    }

    @Test
    void leavesOtherModsBucketsAndOrdinaryMineralItemsUntouched() {
        CompoundNBT copper = bucket("copper");
        CompoundNBT original = copper.copy();
        LegacyWorldUpgrade.migrateItems(copper);
        assertEquals(original, copper);

        CompoundNBT powder = new CompoundNBT();
        powder.putString("id", "baseminerals:lithium_powder");
        powder.putByte("Count", (byte) 3);
        original = powder.copy();
        LegacyWorldUpgrade.migrateItems(powder);
        assertEquals(original, powder);
    }

    private static CompoundNBT bucket(String fluid) {
        CompoundNBT stack = new CompoundNBT();
        stack.putString("id", "forge:bucketfilled");
        stack.putByte("Count", (byte) 1);
        CompoundNBT tag = new CompoundNBT();
        tag.putString("FluidName", fluid);
        tag.putInt("Amount", 1000);
        tag.putString("Custom", "Keep me");
        stack.put("tag", tag);
        return stack;
    }
}
