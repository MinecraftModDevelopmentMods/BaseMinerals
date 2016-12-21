package mmd.baseminerals;

import mmd.baseminerals.proxy.CommonProxy;
import net.minecraftforge.fml.common.*;
import net.minecraftforge.fml.common.Mod.*;
import net.minecraftforge.fml.common.event.*;

/**
 * This is the entry point for this Mod.
 * @author Jasmine Iwanek
 *
 */
@Mod(modid = BaseMinerals.MODID, name = BaseMinerals.NAME, version = BaseMinerals.VERSION, dependencies = "required-after:Forge@[12.17.0.1976,);required-after:basemetals;before:buildingbricks", acceptedMinecraftVersions = "[1.10.2,)", updateJSON = "https://raw.githubusercontent.com/MinecraftModDevelopment/BaseMinerals/master/update.json")
public class BaseMinerals {

	@Instance
	public static BaseMinerals INSTANCE = null;

	/** ID of this Mod */
	public static final String MODID = "baseminerals";

	/** Display name of this Mod */
	public static final String NAME = "Base Minerals";

	/**
	 * Version number, in Major.Minor.Build format. The minor number is
	 * increased whenever a change is made that has the potential to break
	 * compatibility with other mods that depend on this one.
	 */
	public static final String VERSION = "0.12.0-beta1";

	@SidedProxy(clientSide = "baseminerals.proxy.ClientProxy", serverSide = "baseminerals.proxy.CommonProxy")
	public static CommonProxy PROXY = null;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		INSTANCE = this;
		PROXY.preInit(event);
	}

	@EventHandler
	public void onRemap(FMLMissingMappingsEvent event) {
		PROXY.onRemap(event);
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		PROXY.init(event);
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		PROXY.postInit(event);
	}
}
