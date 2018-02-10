package com.mcmoddev.baseminerals.init;

import com.mcmoddev.baseminerals.data.AchievementNames;
import com.mcmoddev.baseminerals.data.MaterialNames;
import com.mcmoddev.lib.data.Names;
import com.mcmoddev.lib.data.SharedStrings;

import net.minecraftforge.common.AchievementPage;
import net.minecraftforge.fml.common.Loader;

/** initializer for achievements */
public class Achievements extends com.mcmoddev.lib.init.Achievements {

	private Achievements() {
		throw new IllegalAccessError(SharedStrings.NOT_INSTANTIABLE);
	}

	/**
	 *
	 */
	public static void init() {
		if (com.mcmoddev.lib.util.ConfigBase.Options.enableAchievements()) {
			AchievementPage page = new AchievementPage(Loader.instance().activeModContainer().getModId());
			AchievementPage.registerAchievementPage(page);

			if (Materials.hasMaterial(MaterialNames.SILICON)) {
				makeAchievement(AchievementNames.SILICON_MAKER, getAchievementByName("metallurgy"), 0, 0, Materials.getMaterialByName(MaterialNames.SILICON).getItem(Names.INGOT), page);
			}
			// TODO: Add lithium
		}
	}
}
