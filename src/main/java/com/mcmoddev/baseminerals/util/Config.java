package com.mcmoddev.baseminerals.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashSet;

import com.mcmoddev.baseminerals.BaseMinerals;

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
	}

	public static void postInit() {
		CrusherRecipeRegistry.getInstance().clearCache();
	}
}
