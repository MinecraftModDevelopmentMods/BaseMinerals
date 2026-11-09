package zone.moddev.mc.baseminerals.util;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import com.mcmoddev.lib.registry.CrusherRecipeRegistry;
import com.mcmoddev.lib.util.Config;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import zone.moddev.mc.baseminerals.BaseMinerals;
import zone.moddev.mc.baseminerals.data.MaterialNames;

/** Saves requested settings and latches registration choices until restart. */
public class BMiConfig extends Config {
    public static final String MATERIALS = "Metals";
    public static final String VANILLA = "Vanilla";
    public static final String FLUIDS = "Fluids";
    private static final Map<String, Boolean> active = new HashMap<>();
    private static Configuration configuration;

    public static void init() {
        if (configuration != null) return;
        configuration = new Configuration(new File("config/BaseMinerals.cfg"));
        load(configuration);
        if (configuration.hasChanged()) configuration.save();
        MinecraftForge.EVENT_BUS.register(new BMiConfig());
    }

    static void load(Configuration value) {
        for (String name : new String[]{MaterialNames.CHARCOAL, MaterialNames.LITHIUM,
                MaterialNames.NITER, MaterialNames.PHOSPHORUS, MaterialNames.POTASH, MaterialNames.SALT,
                MaterialNames.SALTPETER, MaterialNames.SILICON, MaterialNames.SULFUR}) {
            String title = Character.toUpperCase(name.charAt(0)) + name.substring(1);
            boolean charcoal = MaterialNames.CHARCOAL.equals(name);
            Property material = value.get(charcoal ? VANILLA : MATERIALS, "Enable" + title, true);
            material.setRequiresMcRestart(true);
            material.setLanguageKey("config.baseminerals.enable_" + name);
            boolean enabled = material.getBoolean();
            active.put(name, enabled);
            if (!charcoal) Options.materialEnabled(name, enabled);
            if (MaterialNames.LITHIUM.equals(name) || MaterialNames.SILICON.equals(name)) {
                Property fluid = value.get(FLUIDS, "Enabled " + title, true);
                fluid.setRequiresMcRestart(true);
                fluid.setLanguageKey("config.baseminerals.fluid_" + name);
                Options.fluidEnabled(name, enabled && fluid.getBoolean());
            }
        }
    }

    public static boolean isEnabled(String name) {
        return Boolean.TRUE.equals(active.get(name));
    }

    public static Configuration getConfiguration() {
        if (configuration == null) init();
        return configuration;
    }

    @SubscribeEvent
    public void onConfigChange(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (BaseMinerals.MODID.equals(event.getModID())) configuration.save();
    }

    public static void postInit() {
        CrusherRecipeRegistry.getInstance().clearCache();
    }
}
