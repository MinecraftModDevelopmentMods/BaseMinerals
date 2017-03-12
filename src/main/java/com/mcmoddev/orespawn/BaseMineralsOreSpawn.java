package com.mcmoddev.orespawn;

import com.google.common.base.Function;
import com.mcmoddev.baseminerals.init.Materials;
import mmd.orespawn.api.OreSpawnAPI;
import mmd.orespawn.api.SpawnLogic;
import com.mcmoddev.baseminerals.util.Config.Options;

public class BaseMineralsOreSpawn implements Function<OreSpawnAPI, SpawnLogic> {

	@Override
	public SpawnLogic apply(OreSpawnAPI api) {
		SpawnLogic logic = api.createSpawnLogic();

		if( Options.enableLithium ) {
			logic.getDimension(OreSpawnAPI.DIMENSION_WILDCARD).addOre(Materials.lithium.ore.getDefaultState(), 8, 4, 8, 0,  96);
		}

		if( Options.enableNiter ) {
			logic.getDimension(OreSpawnAPI.DIMENSION_WILDCARD).addOre(Materials.niter.ore.getDefaultState(), 8, 4,  8, 0,  96);
		}

		if( Options.enableSalt ) {
			logic.getDimension(OreSpawnAPI.DIMENSION_WILDCARD).addOre(Materials.salt.ore.getDefaultState(), 8, 4, 8, 0, 96);
		}

		if( Options.enableSulfur ) {
			logic.getDimension(OreSpawnAPI.DIMENSION_WILDCARD).addOre(Materials.sulfur.ore.getDefaultState(), 8, 4, 6, 0, 32);
		}

		if( Options.enablePhosphorus ) {
			logic.getDimension(OreSpawnAPI.DIMENSION_WILDCARD).addOre(Materials.phosphorus.ore.getDefaultState(), 8, 4, 6, 0, 96);
		}

		if( Options.enablePotash ) {
			logic.getDimension(OreSpawnAPI.DIMENSION_WILDCARD).addOre(Materials.potash.ore.getDefaultState(), 8, 4, 6, 0, 96);
		}

		if( Options.enableSaltpeter ) {
			logic.getDimension(OreSpawnAPI.DIMENSION_WILDCARD).addOre(Materials.saltpeter.ore.getDefaultState(), 8, 4, 6, 0, 96);
		}


		return logic;
	}
}
