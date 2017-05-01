package com.mcmoddev.baseminerals.init;

import com.mcmoddev.baseminerals.util.Config.Options;
import com.mcmoddev.lib.data.Names;

import net.minecraft.stats.Achievement;
import net.minecraftforge.common.AchievementPage;
import net.minecraftforge.fml.common.Loader;

/** initializer for achievements */
public class Achievements extends com.mcmoddev.lib.init.Achievements {

	/** Make Silicon Blend */
	public static Achievement siliconMaker;

	private static boolean initDone = false;

	private Achievements() {
		throw new IllegalAccessError("Not a instantiable class");
	}

	/**
	 *
	 */
	public static void init() {
		if (initDone) {
			return;
		}

		if (com.mcmoddev.basemetals.util.Config.Options.enableAchievements()) {
			AchievementPage page = new AchievementPage(Loader.instance().activeModContainer().getModId());
			AchievementPage.registerAchievementPage(page);

			if (Options.enableSilicon) {
				siliconMaker = makeAchievement("silicon_maker", com.mcmoddev.basemetals.init.Achievements.getAchievementByName("metallurgy"), 0, 0, Materials.getMaterialByName("silicon").getItem(Names.INGOT), page);
			}
		}

		initDone = true;
	}
}
