package com.mcmoddev.baseminerals.util;

import com.mcmoddev.baseminerals.data.AchievementNames;
import com.mcmoddev.baseminerals.data.MaterialNames;
import com.mcmoddev.lib.init.Achievements;
import com.mcmoddev.lib.item.ItemMMDIngot;
import com.mcmoddev.lib.material.IMMDObject;
import com.mcmoddev.lib.util.ConfigBase.Options;

import net.minecraft.item.Item;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.ItemSmeltedEvent;

public class EventHandler {

	@SubscribeEvent
	void event(ItemSmeltedEvent event) {
		if (!(Options.enableAchievements())) {
			return;
		}

		final Item item = event.smelting.getItem();
		if (!(item instanceof IMMDObject)) {
			return;
		}

		final String materialName = ((IMMDObject) item).getMMDMaterial().getName();
		if (item instanceof ItemMMDIngot) {
			if (materialName.equals(MaterialNames.SILICON)) {
				event.player.addStat(Achievements.getAchievementByName(AchievementNames.SILICON_MAKER), 1);
			}
		}
	}
}
