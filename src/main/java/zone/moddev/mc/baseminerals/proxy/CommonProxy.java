package zone.moddev.mc.baseminerals.proxy;


import zone.moddev.mc.baseminerals.data.MaterialNames;
import zone.moddev.mc.baseminerals.init.ItemGroups;
import zone.moddev.mc.baseminerals.init.Recipes;
import zone.moddev.mc.baseminerals.init.VillagerTrades;
import zone.moddev.mc.baseminerals.util.BMiConfig;
import zone.moddev.mc.baseminerals.worldgen.BaseMineralsWorldgenProvider;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

/**
 * Base Minerals Common Proxy
 *
 * @author Jasmine Iwanek
 *
 */
public class CommonProxy {

	@SuppressWarnings("deprecation")
	public void preInit(FMLPreInitializationEvent event) {
		VillagerTrades.init();
		ItemGroups.init();
	}


	public void init(FMLInitializationEvent event) {
		BaseMineralsWorldgenProvider.enqueue();
		Recipes.init();

		ItemGroups.setupIcons(MaterialNames.SILICON);
	}

	public void postInit(FMLPostInitializationEvent event) {
		BMiConfig.postInit();
	}
}
