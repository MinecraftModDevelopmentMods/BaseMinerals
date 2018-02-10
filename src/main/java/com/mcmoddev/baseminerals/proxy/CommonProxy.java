package com.mcmoddev.baseminerals.proxy;

import java.util.HashSet;

import com.mcmoddev.baseminerals.BaseMinerals;
import com.mcmoddev.baseminerals.init.*;
import com.mcmoddev.baseminerals.util.Config;
import com.mcmoddev.lib.data.SharedStrings;
import com.mcmoddev.lib.integration.IntegrationManager;
import com.mcmoddev.lib.oregen.FallbackGenerator;
import com.mcmoddev.lib.util.ConfigBase.Options;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
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

	public void preInit(FMLPreInitializationEvent event) {

		Config.init();

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

		Materials.init();
		Fluids.init();
		ItemGroups.init();
		Blocks.init();
		Items.init();
		VillagerTrades.init();
		ItemGroups.setupIcons();

		MinecraftForge.EVENT_BUS.register(com.mcmoddev.baseminerals.util.EventHandler.class);

		IntegrationManager.INSTANCE.preInit(event);
	}

	public void init(FMLInitializationEvent event) {
		Recipes.init();

	}

	public void postInit(FMLPostInitializationEvent event) {
		Config.postInit();
	}

	public void onRemapBlock(RegistryEvent.MissingMappings<Block> event) {
		// Currently Unused
	}

	public void onRemapItem(RegistryEvent.MissingMappings<Item> event) {
		// Currently Unused
	}
}
