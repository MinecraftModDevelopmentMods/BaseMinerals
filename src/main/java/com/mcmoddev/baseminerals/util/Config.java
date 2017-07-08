package com.mcmoddev.baseminerals.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashSet;

import com.mcmoddev.baseminerals.BaseMinerals;
import com.mcmoddev.baseminerals.data.AdditionalLootTables;

import com.mcmoddev.lib.registry.CrusherRecipeRegistry;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.MissingModsException;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.versioning.ArtifactVersion;
import net.minecraftforge.fml.common.versioning.DefaultArtifactVersion;

/**
 * @author Jasmine Iwanek
 *
 */
public class Config extends com.mcmoddev.lib.util.ConfigBase {

	private static Configuration configuration;
	private static final String CONFIG_FILE = "config/BaseMinerals.cfg";
	private static final String MATERIALS_CAT = "Minerals";
	private static final String ALT_CFG_PATH = "config/additional-loot-tables";

	@SubscribeEvent
	public void onConfigChange(ConfigChangedEvent.OnConfigChangedEvent e) {
		if (e.getModID().equals(BaseMinerals.MODID)) {
			init();
		}
	}

	public static void init() {
		if (configuration == null) {
			configuration = new Configuration(new File(CONFIG_FILE));
			MinecraftForge.EVENT_BUS.register(new Config());
		}

		// METALS
		Options.materialEnabled("Charcoal", configuration.getBoolean("EnableCharcoal", MATERIALS_CAT, true,
				"Enable Charcoal Items and Materials"));
		Options.materialEnabled("Lithium", configuration.getBoolean("EnableLithium", MATERIALS_CAT, true,
				"Enable Lithium Items and Materials"));
		Options.materialEnabled("Niter", configuration.getBoolean("EnableNiter", MATERIALS_CAT, true,
				"Enable Niter Items and Materials"));
		Options.materialEnabled("Phosphorus", configuration.getBoolean("EnablePhosphorus", MATERIALS_CAT, true,
				"Enable Phosphorus Items and Materials"));
		Options.materialEnabled("Potash", configuration.getBoolean("EnablePotash", MATERIALS_CAT, true,
				"Enable Potash Items and Materials"));
		Options.materialEnabled("Salt", configuration.getBoolean("EnableSalt", MATERIALS_CAT, true,
				"Enable Salt Items and Materials"));
		Options.materialEnabled("Saltpeter", configuration.getBoolean("EnableSaltpeter", MATERIALS_CAT, true,
				"Enable Saltpeter Items and Materials"));
		Options.materialEnabled("Silicon", configuration.getBoolean("EnableSilicon", MATERIALS_CAT, true,
				"Enable Silicon Items and Materials"));
		Options.materialEnabled("Sulfur", configuration.getBoolean("EnableSulfur", MATERIALS_CAT, true,
				"Enable Sulfur Items and Materials"));

		if (configuration.hasChanged()) {
			configuration.save();
		}

		if ((com.mcmoddev.basemetals.util.Config.Options.requireMMDOreSpawn()) && (!Loader.isModLoaded("orespawn"))) {
			final HashSet<ArtifactVersion> orespawnMod = new HashSet<>();
			orespawnMod.add(new DefaultArtifactVersion("3.1.0"));
			throw new MissingModsException(orespawnMod, "orespawn", "MMD Ore Spawn Mod");
		}

		final Path myLootFolder = Paths.get(ALT_CFG_PATH, BaseMinerals.MODID);
		if (!(myLootFolder.toFile().exists())) {
			try {
				final String chests = "chests";
				Files.createDirectories(myLootFolder.resolve(chests));
				Files.write(myLootFolder.resolve(chests).resolve("abandoned_mineshaft.json"),
						Collections.singletonList(AdditionalLootTables.ABANDONED_MINESHAFT));
				Files.write(myLootFolder.resolve(chests).resolve("desert_pyramid.json"),
						Collections.singletonList(AdditionalLootTables.DESERT_PYRAMID));
				Files.write(myLootFolder.resolve(chests).resolve("end_city_treasure.json"),
						Collections.singletonList(AdditionalLootTables.END_CITY_TREASURE));
				Files.write(myLootFolder.resolve(chests).resolve("jungle_temple.json"),
						Collections.singletonList(AdditionalLootTables.JUNGLE_TEMPLE));
				Files.write(myLootFolder.resolve(chests).resolve("nether_bridge.json"),
						Collections.singletonList(AdditionalLootTables.NETHER_BRIDGE));
				Files.write(myLootFolder.resolve(chests).resolve("simple_dungeon.json"),
						Collections.singletonList(AdditionalLootTables.SIMPLE_DUNGEON));
				Files.write(myLootFolder.resolve(chests).resolve("spawn_bonus_chest.json"),
						Collections.singletonList(AdditionalLootTables.SPAWN_BONUS_CHEST));
				Files.write(myLootFolder.resolve(chests).resolve("stronghold_corridor.json"),
						Collections.singletonList(AdditionalLootTables.STRONGHOLD_CORRIDOR));
				Files.write(myLootFolder.resolve(chests).resolve("stronghold_crossing.json"),
						Collections.singletonList(AdditionalLootTables.STRONGHOLD_CROSSING));
				Files.write(myLootFolder.resolve(chests).resolve("village_blacksmith.json"),
						Collections.singletonList(AdditionalLootTables.VILLAGE_BLACKSMITH));
			} catch (final IOException ex) {
				BaseMinerals.logger.error("Failed to extract additional loot tables", ex);
			}
		}
	}

	public static void postInit() {
		CrusherRecipeRegistry.getInstance().clearCache();
	}
}
