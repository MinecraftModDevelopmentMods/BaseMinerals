package zone.moddev.mc.baseminerals;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import zone.moddev.mc.baseminerals.util.BMiConfig;
import zone.moddev.mc.baseminerals.proxy.CommonProxy;
import com.mcmoddev.lib.data.SharedStrings;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLConstructionEvent;
import net.minecraftforge.fml.common.event.FMLFingerprintViolationEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
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
		dependencies = "required-after:forge@[14.23.5.2859,);required-after:mmdlib@[1.0.0-rc2,2.0.0);required-after:orespawn@[4.1.6.112021,5.0.0);after:basemetals;after:tconstruct;after:enderio;before:appliedenergistics2;before:buildingbricks",
		acceptedMinecraftVersions = "[1.12.2]",
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
	public static final String VERSION = "2.5.1.112021";

	protected static final String UPDATEJSON = SharedStrings.UPDATE_JSON_URL + "BaseMinerals/master-1.12/update.json";

	private static final String PROXY_BASE = "zone.moddev.mc.baseminerals.proxy.";

	@SidedProxy(clientSide = PROXY_BASE + SharedStrings.CLIENTPROXY, serverSide = PROXY_BASE
		+ SharedStrings.SERVERPROXY)
	public static CommonProxy proxy;

	public static final Logger logger = LogManager.getFormatterLogger(BaseMinerals.MODID);

	@EventHandler
	public void onFingerprintViolation(FMLFingerprintViolationEvent event) {
		logger.warn(SharedStrings.INVALID_FINGERPRINT);
	}

	@EventHandler
	public static void constructing(FMLConstructionEvent event) {
		BMiConfig.init();
	}
	
	@EventHandler
	public static void preInit(FMLPreInitializationEvent event) {
		proxy.preInit(event);

		MinecraftForge.EVENT_BUS.register(zone.moddev.mc.baseminerals.init.Items.class);
		MinecraftForge.EVENT_BUS.register(zone.moddev.mc.baseminerals.init.Blocks.class);
	}

	@EventHandler
	public static void init(FMLInitializationEvent event) {
		proxy.init(event);
	}

	@EventHandler
	public static void postInit(FMLPostInitializationEvent event) {
		proxy.postInit(event);
	}

}
