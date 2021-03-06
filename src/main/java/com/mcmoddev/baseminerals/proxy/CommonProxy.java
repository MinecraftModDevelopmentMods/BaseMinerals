package com.mcmoddev.baseminerals.proxy;

import java.util.HashSet;

import com.mcmoddev.baseminerals.data.MaterialNames;
import com.mcmoddev.baseminerals.init.ItemGroups;
import com.mcmoddev.baseminerals.init.Recipes;
import com.mcmoddev.baseminerals.init.VillagerTrades;
import com.mcmoddev.baseminerals.util.BMiConfig;
import com.mcmoddev.lib.data.SharedStrings;
import com.mcmoddev.lib.oregen.FallbackGenerator;
import com.mcmoddev.lib.util.Config.Options;

import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.MissingModsException;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.versioning.ArtifactVersion;
import net.minecraftforge.fml.common.versioning.DefaultArtifactVersion;

/**
 * Base Minerals Common Proxy
 *
 * @author Jasmine Iwanek
 *
 */
public class CommonProxy {

	@SuppressWarnings("deprecation")
	public void preInit(FMLPreInitializationEvent event) {
		if ((Options.requireMMDOreSpawn()) && (!Loader.isModLoaded(SharedStrings.ORESPAWN_MODID))) {
			if (Options.fallbackOrespawn()) {
				GameRegistry.registerWorldGenerator(new FallbackGenerator(), 0);
			} else {
				final HashSet<ArtifactVersion> orespawnMod = new HashSet<>();
				orespawnMod.add(new DefaultArtifactVersion(SharedStrings.ORESPAWN_VERSION));
				throw new MissingModsException(orespawnMod, SharedStrings.ORESPAWN_MODID,
						SharedStrings.ORESPAWN_MISSING_TEXT);
			}
		}

		VillagerTrades.init();
		ItemGroups.init();
	}


	public void init(FMLInitializationEvent event) {
		Recipes.init();

		ItemGroups.setupIcons(MaterialNames.SILICON);
	}

	public void postInit(FMLPostInitializationEvent event) {
		BMiConfig.postInit();
	}
}
