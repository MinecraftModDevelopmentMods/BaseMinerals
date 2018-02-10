package com.mcmoddev.orespawn;

import com.mcmoddev.baseminerals.BaseMinerals;
import com.mcmoddev.lib.data.SharedStrings;
import com.mcmoddev.orespawn.api.os3.OS3API;
import com.mcmoddev.orespawn.api.plugin.IOreSpawnPlugin;
import com.mcmoddev.orespawn.api.plugin.OreSpawnPlugin;

@OreSpawnPlugin(modid = BaseMinerals.MODID, resourcePath = SharedStrings.ORESPAWN_MODID)
public class BaseMineralsOreSpawn implements IOreSpawnPlugin {

	@Override
	public void register(OS3API apiInterface) {
		// nothing for us to do - all of our ores are in the
		// jar and the code handles that
	}
}
