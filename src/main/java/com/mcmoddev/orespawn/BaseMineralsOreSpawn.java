package com.mcmoddev.orespawn;

import com.google.common.base.Function;
import com.mcmoddev.baseminerals.init.Materials;
import com.mcmoddev.orespawn.api.OreSpawnAPI;
import com.mcmoddev.orespawn.api.SpawnLogic;
import com.mcmoddev.baseminerals.util.Config.Options;
import com.mcmoddev.lib.data.Names;

public class BaseMineralsOreSpawn implements Function<OreSpawnAPI, SpawnLogic> {

	@Override
	public SpawnLogic apply(OreSpawnAPI api) {
		SpawnLogic logic = api.createSpawnLogic();

		if( Options.enableLithium ) {
			logic.getDimension(OreSpawnAPI.DIMENSION_WILDCARD).addOre(Materials.getMaterialByName("lithium").getBlock(Names.ORE).getDefaultState(), 8, 4, 8, 0,  96);
		}

		if( Options.enableNiter ) {
			logic.getDimension(OreSpawnAPI.DIMENSION_WILDCARD).addOre(Materials.getMaterialByName("niter").getBlock(Names.ORE).getDefaultState(), 8, 4,  8, 0,  96);
		}

		if( Options.enableSalt ) {
			logic.getDimension(OreSpawnAPI.DIMENSION_WILDCARD).addOre(Materials.getMaterialByName("salt").getBlock(Names.ORE).getDefaultState(), 8, 4, 8, 0, 96);
		}

		if( Options.enableSulfur ) {
			logic.getDimension(OreSpawnAPI.DIMENSION_WILDCARD).addOre(Materials.getMaterialByName("sulfur").getBlock(Names.ORE).getDefaultState(), 8, 4, 6, 0, 32);
		}

		if( Options.enablePhosphorus ) {
			logic.getDimension(OreSpawnAPI.DIMENSION_WILDCARD).addOre(Materials.getMaterialByName("phosphorus").getBlock(Names.ORE).getDefaultState(), 8, 4, 6, 0, 96);
		}

		if( Options.enablePotash ) {
			logic.getDimension(OreSpawnAPI.DIMENSION_WILDCARD).addOre(Materials.getMaterialByName("potash").getBlock(Names.ORE).getDefaultState(), 8, 4, 6, 0, 96);
		}

		if( Options.enableSaltpeter ) {
			logic.getDimension(OreSpawnAPI.DIMENSION_WILDCARD).addOre(Materials.getMaterialByName("saltpeter").getBlock(Names.ORE).getDefaultState(), 8, 4, 6, 0, 96);
		}


		return logic;
	}
}
