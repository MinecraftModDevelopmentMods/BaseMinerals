package com.mcmoddev.orespawn;

import com.google.common.base.Function;
import com.mcmoddev.baseminerals.init.Materials;
import com.mcmoddev.orespawn.api.OreSpawnAPI;
import com.mcmoddev.orespawn.api.SpawnLogic;

public class BaseMineralsOreSpawn implements Function<OreSpawnAPI, SpawnLogic> {

	@Override
	public SpawnLogic apply(OreSpawnAPI api) {
		SpawnLogic logic = api.createSpawnLogic();

		logic.getDimension(OreSpawnAPI.DIMENSION_WILDCARD)
				.addOre(Materials.lithium.ore.getDefaultState(), 8, 4, 8, 0,  96)
				.addOre(Materials.niter.ore.getDefaultState(), 8, 4,  8, 0,  96)
				.addOre(Materials.salt.ore.getDefaultState(), 8, 4, 8, 0, 96)
				.addOre(Materials.sulfur.ore.getDefaultState(), 8, 4, 6, 0, 32)
				.addOre(Materials.phosphorus.ore.getDefaultState(), 8, 4, 6, 0, 96)
				.addOre(Materials.potash.ore.getDefaultState(), 8, 4, 6, 0, 96)
				.addOre(Materials.saltpeter.ore.getDefaultState(), 8, 4, 6, 0, 96);

		return logic;
	}
}
