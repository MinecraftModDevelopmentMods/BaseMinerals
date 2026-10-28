package zone.moddev.mc.baseminerals;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import zone.moddev.mc.baseminerals.proxy.CommonProxy;
import com.mcmoddev.lib.data.SharedStrings;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLFingerprintViolationEvent;
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
		dependencies = "required-after:Forge@[12.18.3.2511,);required-after:basemetals@[2.5.1.110021,2.6.0);required-after:orespawn@[4.1.2.110021,5.0.0);after:tconstruct;after:enderio;before:buildingbricks",
		acceptedMinecraftVersions = "[1.10.2]",
        guiFactory = "zone.moddev.mc.baseminerals.client.BaseMineralsGuiFactory",
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
	public static final String VERSION = "2.5.1.110021";

	protected static final String UPDATEJSON = "https://raw.githubusercontent.com/MinecraftModDevelopmentMods/BaseMinerals/master-1.10.2/update.json";

	private static final String PROXY_BASE = "zone.moddev.mc.baseminerals.proxy.";

	@SidedProxy(clientSide = PROXY_BASE + SharedStrings.CLIENTPROXY, serverSide = PROXY_BASE
		+ SharedStrings.SERVERPROXY)
	public static CommonProxy proxy;

	public static final Logger logger = LogManager.getFormatterLogger(BaseMinerals.MODID);


    /** Kept for old binary callers; unsigned jars do not subscribe to this event. */
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
		proxy.onRemap(event);
	}
}
