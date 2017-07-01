package com.mcmoddev.orespawn;

import com.google.common.base.Function;
import com.mcmoddev.baseminerals.data.MaterialNames;
import com.mcmoddev.baseminerals.init.Materials;
import com.mcmoddev.orespawn.api.OreSpawnAPI;
import com.mcmoddev.orespawn.api.SpawnLogic;
import com.mcmoddev.baseminerals.util.Config.Options;
import com.mcmoddev.lib.data.Names;

public class BaseMineralsOreSpawn implements Function<OreSpawnAPI, SpawnLogic> {

	@Override
	public SpawnLogic apply(OreSpawnAPI api) {
		SpawnLogic logic = api.createSpawnLogic();

		if (Options.materialEnabled(MaterialNames.LITHIUM)) {
			logic.getDimension(OreSpawnAPI.DIMENSION_WILDCARD).addOre(Materials.getMaterialByName(MaterialNames.LITHIUM).getBlock(Names.ORE).getDefaultState(), 8, 4, 8, 0,  96);
		}

		if (Options.materialEnabled(MaterialNames.NITER)) {
			logic.getDimension(OreSpawnAPI.DIMENSION_WILDCARD).addOre(Materials.getMaterialByName(MaterialNames.NITER).getBlock(Names.ORE).getDefaultState(), 8, 4,  8, 0,  96);
		}

		if (Options.materialEnabled(MaterialNames.SALT)) {
			logic.getDimension(OreSpawnAPI.DIMENSION_WILDCARD).addOre(Materials.getMaterialByName(MaterialNames.SALT).getBlock(Names.ORE).getDefaultState(), 8, 4, 8, 0, 96);
		}

		if (Options.materialEnabled(MaterialNames.SULFUR)) {
			logic.getDimension(OreSpawnAPI.DIMENSION_WILDCARD).addOre(Materials.getMaterialByName(MaterialNames.SULFUR).getBlock(Names.ORE).getDefaultState(), 8, 4, 6, 0, 32);
		}

		if (Options.materialEnabled(MaterialNames.PHOSPHORUS)) {
			logic.getDimension(OreSpawnAPI.DIMENSION_WILDCARD).addOre(Materials.getMaterialByName(MaterialNames.PHOSPHORUS).getBlock(Names.ORE).getDefaultState(), 8, 4, 6, 0, 96);
		}

		if (Options.materialEnabled(MaterialNames.POTASH)) {
			logic.getDimension(OreSpawnAPI.DIMENSION_WILDCARD).addOre(Materials.getMaterialByName(MaterialNames.POTASH).getBlock(Names.ORE).getDefaultState(), 8, 4, 6, 0, 96);
		}

		if (Options.materialEnabled(MaterialNames.SALTPETER)) {
			logic.getDimension(OreSpawnAPI.DIMENSION_WILDCARD).addOre(Materials.getMaterialByName(MaterialNames.SALTPETER).getBlock(Names.ORE).getDefaultState(), 8, 4, 6, 0, 96);
		}


		return logic;
	}
}
