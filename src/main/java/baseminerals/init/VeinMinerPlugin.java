package baseminerals.init;

import portablejim.veinminer.api.IMCMessage;

import org.apache.logging.log4j.Level;

import baseminerals.BaseMinerals;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.Loader;

/**
 * 
 * @author Jasmine Iwanek
 *
 */
public class VeinMinerPlugin {

	private static boolean initDone = false;

	/**
	 *
	 */
	public static void init() {
		if(initDone)
			return;

		if(Loader.isModLoaded("VeinMiner") || Loader.isModLoaded("veinminer")) {
			FMLLog.log(Level.ERROR, "%s: Activating VeinMiner Support", BaseMinerals.MODID);
			
			addToolsForMetal("aluminum");
			addToolsForMetal("aluminumbrass");
			addToolsForMetal("cadmium");
			addToolsForMetal("chromium");
			addToolsForMetal("galvanizedsteel");
			addToolsForMetal("iridium");
			addToolsForMetal("magnesium");
			addToolsForMetal("manganese");
			addToolsForMetal("nichrome");
			addToolsForMetal("osmium");
			addToolsForMetal("plutonium");
			addToolsForMetal("rutile");
			addToolsForMetal("stainlesssteel");
			addToolsForMetal("tantalum");
			addToolsForMetal("titanium");
			addToolsForMetal("tungsten");
			addToolsForMetal("uranium");
			addToolsForMetal("zirconium");
		}

		initDone = true;
	}

	/**
	 *
	 * @param metal
	 */
	public static void addToolsForMetal(String metal) {
		IMCMessage.addTool("axe", BaseMinerals.MODID + ":" + metal + "_axe");
		IMCMessage.addTool("hoe", BaseMinerals.MODID + ":" + metal + "_hoe");
		IMCMessage.addTool("pickaxe", BaseMinerals.MODID + ":" + metal + "_pickaxe");
		IMCMessage.addTool("shears", BaseMinerals.MODID + ":" + metal + "_shears");
		IMCMessage.addTool("shovel", BaseMinerals.MODID + ":" + metal + "_shovel");
//		IMCMessage.addTool("crook", BaseMinerals.MODID + ":" + metal + "_crook");
		IMCMessage.addTool("hammer", BaseMinerals.MODID + ":" + metal + "_hammer");
	}
}
