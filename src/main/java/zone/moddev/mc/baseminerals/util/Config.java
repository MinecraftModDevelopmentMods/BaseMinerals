package zone.moddev.mc.baseminerals.util;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import com.mcmoddev.lib.registry.CrusherRecipeRegistry;
import com.mcmoddev.lib.util.ConfigBase.Options;

/** Persist requested settings, retaining the active registration choices until restart. */
public class Config extends com.mcmoddev.lib.util.ConfigBase {
    private static Configuration configuration;
    private static final Map<String,Boolean> active=new HashMap<>();
    public static void init() {
        if(configuration!=null) return;
        configuration=new Configuration(new File("config/BaseMinerals.cfg"));
        load(configuration);
        if(configuration.hasChanged()) configuration.save();
        MinecraftForge.EVENT_BUS.register(new Config());
    }
    static void load(Configuration value) {
        for(String name:new String[]{"charcoal","lithium","niter","phosphorus","potash","salt","saltpeter","silicon","sulfur"}) {
            String title=Character.toUpperCase(name.charAt(0))+name.substring(1);
            Property property=value.get("Minerals","Enable"+title,true,"Enable "+title+" Items and Materials. Requires a game restart.");
            property.setRequiresMcRestart(true);
            property.setLanguageKey("config.baseminerals.enable_"+name);
            boolean enabled=property.getBoolean(); active.put(name,enabled);
            // Charcoal belongs to Base Metals. This add-on must not disable it globally.
            if(!"charcoal".equals(name)) Options.materialEnabled(name,enabled);
            if("lithium".equals(name)||"silicon".equals(name)) Options.fluidEnabled(name,enabled);
        }
    }
    public static boolean isEnabled(String name) {return Boolean.TRUE.equals(active.get(name));}
    public static Configuration getConfiguration() {if(configuration==null)init();return configuration;}
    @SubscribeEvent public void onConfigChange(ConfigChangedEvent.OnConfigChangedEvent event) {
        if("baseminerals".equals(event.getModID())) configuration.save();
    }
    public static void postInit() {CrusherRecipeRegistry.getInstance().clearCache();}
}
