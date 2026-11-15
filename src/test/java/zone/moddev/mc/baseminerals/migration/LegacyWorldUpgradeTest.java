package zone.moddev.mc.baseminerals.migration;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class LegacyWorldUpgradeTest {
    @Test
    void convertsNestedMineralBucketsAndKeepsTheRestOfTheStack() {
        NBTTagCompound lithium = bucket("lithium");
        NBTTagCompound silicon = bucket("baseminerals:silicon");
        NBTTagList inventory = new NBTTagList();
        inventory.add(lithium);
        inventory.add(silicon);
        NBTTagCompound player = new NBTTagCompound();
        player.setTag("Inventory", inventory);

        LegacyWorldUpgrade.migrateItems(player);
        NBTTagCompound afterFirstPass = player.copy();
        LegacyWorldUpgrade.migrateItems(player);

        assertEquals(afterFirstPass, player);
        assertEquals("baseminerals:lithium_bucket", lithium.getString("id"));
        assertEquals("baseminerals:silicon_bucket", silicon.getString("id"));
        for (NBTTagCompound stack : new NBTTagCompound[] {lithium, silicon}) {
            assertEquals(1, stack.getByte("Count"));
            assertEquals("Keep me", stack.getCompound("tag").getString("Custom"));
            assertFalse(stack.getCompound("tag").contains("FluidName", 8));
            assertFalse(stack.getCompound("tag").contains("Amount", 99));
        }
    }

    @Test
    void leavesOtherModsBucketsAndOrdinaryMineralItemsUntouched() {
        NBTTagCompound copper = bucket("copper");
        NBTTagCompound original = copper.copy();
        LegacyWorldUpgrade.migrateItems(copper);
        assertEquals(original, copper);

        NBTTagCompound powder = new NBTTagCompound();
        powder.setString("id", "baseminerals:lithium_powder");
        powder.setByte("Count", (byte) 3);
        original = powder.copy();
        LegacyWorldUpgrade.migrateItems(powder);
        assertEquals(original, powder);
    }

    private static NBTTagCompound bucket(String fluid) {
        NBTTagCompound stack = new NBTTagCompound();
        stack.setString("id", "forge:bucketfilled");
        stack.setByte("Count", (byte) 1);
        NBTTagCompound tag = new NBTTagCompound();
        tag.setString("FluidName", fluid);
        tag.setInt("Amount", 1000);
        tag.setString("Custom", "Keep me");
        stack.setTag("tag", tag);
        return stack;
    }
}
