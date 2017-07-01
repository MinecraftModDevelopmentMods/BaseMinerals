package com.mcmoddev.baseminerals.init;

import com.mcmoddev.baseminerals.data.AchievementNames;
import com.mcmoddev.baseminerals.data.MaterialNames;
import com.mcmoddev.baseminerals.util.Config.Options;
import com.mcmoddev.lib.data.Names;

import net.minecraftforge.common.AchievementPage;
import net.minecraftforge.fml.common.Loader;

/** initializer for achievements */
public class Achievements extends com.mcmoddev.lib.init.Achievements {

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

			if (Options.materialEnabled(MaterialNames.SILICON)) {
				makeAchievement(AchievementNames.SILICON_MAKER, getAchievementByName(AchievementNames.METALLURGY), 0, 0, Materials.getMaterialByName(MaterialNames.SILICON).getItem(Names.INGOT), page);
			}
		}

		initDone = true;
	}
}
