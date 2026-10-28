package zone.moddev.mc.baseminerals.proxy;

import java.util.HashSet;

import zone.moddev.mc.baseminerals.BaseMinerals;
import zone.moddev.mc.baseminerals.data.MaterialNames;
import zone.moddev.mc.baseminerals.init.*;
import zone.moddev.mc.baseminerals.util.Config;
import zone.moddev.mc.baseminerals.util.EventHandler;
import com.mcmoddev.lib.data.SharedStrings;
import com.mcmoddev.lib.integration.IntegrationManager;

import com.mcmoddev.lib.util.ConfigBase.Options;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.MissingModsException;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLMissingMappingsEvent;
import net.minecraftforge.fml.common.event.FMLMissingMappingsEvent.MissingMapping;
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


		Materials.init();
		Fluids.init();
		ItemGroups.init();
		Blocks.init();
		Items.init();
		VillagerTrades.init();

		IntegrationManager.INSTANCE.preInit(event);
	}

	public void onRemap(FMLMissingMappingsEvent event) {
		for (final MissingMapping mapping : event.get()) {
			if (mapping.resourceLocation.getResourceDomain().equals(BaseMinerals.MODID)) {
				if (mapping.type.equals(GameRegistry.Type.BLOCK)) {
					// Dummy
				} else if (mapping.type.equals(GameRegistry.Type.ITEM)) {
					// Dummy
				}
			}
		}
	}

	public void init(FMLInitializationEvent event) {
        zone.moddev.mc.baseminerals.worldgen.BaseMineralsWorldgenProvider.enqueue();
		Recipes.init();

		Achievements.init();

		MinecraftForge.EVENT_BUS.register(new EventHandler());

		ItemGroups.setupIcons(MaterialNames.SILICON);
	}

	public void postInit(FMLPostInitializationEvent event) {
		Config.postInit();
	}
}
