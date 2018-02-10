package com.mcmoddev.baseminerals;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mcmoddev.baseminerals.proxy.CommonProxy;
import com.mcmoddev.lib.data.SharedStrings;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLFingerprintViolationEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLMissingMappingsEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

/**
 * This is the entry point for this Mod.
 *
 * @author Jasmine Iwanek
 *
 */
@Mod(
		modid = BaseMinerals.MODID,
		name = BaseMinerals.NAME,
		version = BaseMinerals.VERSION,
		dependencies = "required-after:forge@[13.20.1.2386,);required-after:basemetals;before:appliedenergistics2;before:buildingbricks",
		acceptedMinecraftVersions = "[1.11.2,)",
		certificateFingerprint = "@FINGERPRINT@",
		updateJSON = BaseMinerals.UPDATEJSON)
public class BaseMinerals {

	@Instance
	public static BaseMinerals instance;

	/** ID of this Mod */
	public static final String MODID = "baseminerals";

	/** Display name of this Mod */
	protected static final String NAME = "Base Minerals";

	/**
	 * Version number, in Major.Minor.Build format. The minor number is
	 * increased whenever a change is made that has the potential to break
	 * compatibility with other mods that depend on this one.
	 */
	protected static final String VERSION = "2.5.0-beta4";

	protected static final String UPDATEJSON = SharedStrings.UPDATE_JSON_URL + "BaseMinerals/master/update.json";

	private static final String PROXY_BASE = SharedStrings.MMD_PROXY_GROUP + MODID + SharedStrings.DOT_PROXY_DOT;

	@SidedProxy(clientSide = PROXY_BASE + SharedStrings.CLIENTPROXY, serverSide = PROXY_BASE
		+ SharedStrings.SERVERPROXY)
	public static CommonProxy proxy;

	public static final Logger logger = LogManager.getFormatterLogger(BaseMinerals.MODID);

	@EventHandler
	public void onFingerprintViolation(FMLFingerprintViolationEvent event) {
		logger.warn(SharedStrings.INVALID_FINGERPRINT);
	}

	@EventHandler
	public static void preInit(FMLPreInitializationEvent event) {
		proxy.preInit(event);
	}

	@EventHandler
	public static void init(FMLInitializationEvent event) {
		proxy.init(event);
	}

	@EventHandler
	public static void postInit(FMLPostInitializationEvent event) {
		proxy.postInit(event);
	}

	@EventHandler
	public static void onRemap(FMLMissingMappingsEvent event) {
		// we are not using this right now...
		// proxy.onRemap(event);
	}
}
