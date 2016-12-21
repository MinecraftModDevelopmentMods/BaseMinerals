package mmd.orespawn;

import com.google.common.base.Function;

import mmd.baseminerals.init.Blocks;
import mmd.orespawn.api.OreSpawnAPI;
import mmd.orespawn.api.SpawnLogic;

public class BaseMineralsOreSpawn implements Function<OreSpawnAPI, SpawnLogic> {

	@Override
	public SpawnLogic apply(OreSpawnAPI api) {
		SpawnLogic logic = api.createSpawnLogic();

		logic.getDimension(OreSpawnAPI.DIMENSION_WILDCARD)
				.addOre(Blocks.lithium_ore.getDefaultState(), 8, 4, 8, 0,  96)
				.addOre(Blocks.niter_ore.getDefaultState(), 8, 4,  8, 0,  96)
				.addOre(Blocks.salt_ore.getDefaultState(), 8, 4, 8, 0, 96)
				.addOre(Blocks.sulfur_ore.getDefaultState(), 8, 4, 6, 0, 32)
				.addOre(Blocks.phosphorus_ore.getDefaultState(), 8, 4, 6, 0, 96)
				.addOre(Blocks.potash_ore.getDefaultState(), 8, 4, 6, 0, 96)
				.addOre(Blocks.saltpeter_ore.getDefaultState(), 8, 4, 6, 0, 96);

		return logic;
	}
}
