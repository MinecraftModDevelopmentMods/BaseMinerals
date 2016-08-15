package baseminerals.init;

import baseminerals.BaseMinerals;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Achievement;
import net.minecraftforge.common.AchievementPage;

/** initializer for achievements */
public abstract class Achievements {

	public static AchievementPage page;

	public static Achievement charcoal_maker; // make blend

	private static boolean initDone = false;

	/**
	 * 
	 */
	public static void init() {
		if(initDone)
			return;

		page = new AchievementPage(BaseMinerals.NAME);
		AchievementPage.registerAchievementPage(page);

//		charcoal_maker = makeAchievement("charcoal_maker", cyano.basemetals.init.Achievements.metallurgy, 0, 0, Items.charcoal_ingot);

		initDone = true;
	}

	@SuppressWarnings("unused")
	private static Achievement makeAchievement(String baseName, Achievement requirement, int x, int y, Item icon) {
		return makeAchievement(baseName, requirement, x, y, new ItemStack(icon));
	}

	@SuppressWarnings("unused")
	private static Achievement makeAchievement(String baseName, Achievement requirement, int x, int y, Block icon) {
		return makeAchievement(baseName, requirement, x, y, new ItemStack(icon));
	}

	private static Achievement makeAchievement(String baseName, Achievement requirement, int x, int y, ItemStack icon) {
		Achievement a = new Achievement(baseName, baseName, x, y, icon, requirement).registerStat();
		page.getAchievements().add(a);
		return a;
	}
}
