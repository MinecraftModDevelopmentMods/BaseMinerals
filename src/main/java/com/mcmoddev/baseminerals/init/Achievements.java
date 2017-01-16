package com.mcmoddev.baseminerals.init;

import cyano.basemetals.util.Config.Options;
import net.minecraft.stats.Achievement;
import net.minecraftforge.common.AchievementPage;
import net.minecraftforge.fml.common.Loader;

/** initializer for achievements */
public class Achievements extends cyano.basemetals.init.Achievements {

	public static Achievement charcoal_maker; // make blend

	private static boolean initDone = false;

	/**
	 *
	 */
	public static void init() {
		if (initDone) {
			return;
		}

		if (Options.ENABLE_ACHIEVEMENTS) {
			AchievementPage page = new AchievementPage(Loader.instance().activeModContainer().getModId());
			AchievementPage.registerAchievementPage(page);

			// charcoal_maker = makeAchievement("charcoal_maker", cyano.basemetals.init.Achievements.metallurgy, 0, 0, Items.charcoal_ingot, page);
		}
		initDone = true;
	}
}
