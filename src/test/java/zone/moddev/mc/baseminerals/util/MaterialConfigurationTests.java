package zone.moddev.mc.baseminerals.util;

import static org.junit.jupiter.api.Assertions.*;
import java.nio.file.Path;
import net.minecraftforge.common.config.Configuration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.io.TempDir;

class MaterialConfigurationTests {
    @BeforeEach void freshSharedStartupState() throws Exception {
        for(String key:new String[]{"materialEnabled","fluidEnabled"}) {
            java.lang.reflect.Field field=com.mcmoddev.lib.util.ConfigBase.Options.class.getDeclaredField(key);
            field.setAccessible(true);((java.util.Map<?,?>)field.get(null)).clear();
        }
    }
    @Test void initializesMaterialAndFluidSwitchesTogether() {
        Configuration config=new Configuration();
        Config.load(config);
        for(String name:new String[]{"lithium","silicon"}) {
            assertTrue(Config.isEnabled(name));
            assertTrue(com.mcmoddev.lib.util.ConfigBase.Options.isFluidEnabled(name));
        }
        assertTrue(config.getCategory("Minerals").get("EnableLithium").requiresMcRestart());
    }
    @Test void disabledMaterialAlsoDisablesItsFluidAtStartup() {
        Configuration config=new Configuration();
        config.get("Minerals","EnableLithium",true).set(false);
        Config.load(config);
        assertFalse(Config.isEnabled("lithium"));
        assertFalse(com.mcmoddev.lib.util.ConfigBase.Options.isFluidEnabled("lithium"));
    }
    @Test void charcoalSwitchDoesNotDisableBaseMetalsSharedMaterial() {
        com.mcmoddev.lib.util.ConfigBase.Options.materialEnabled("charcoal",true);
        Configuration config=new Configuration();
        config.get("Minerals","EnableCharcoal",true).set(false);
        Config.load(config);
        assertFalse(Config.isEnabled("charcoal"));
        assertTrue(com.mcmoddev.lib.util.ConfigBase.Options.isMaterialEnabled("charcoal"));
    }
    @Test void editingRequestedSwitchDoesNotChangeActiveRegistration() {
        Configuration config=new Configuration();
        Config.load(config);
        config.get("Minerals","EnableSalt",true).set(false);
        assertTrue(Config.isEnabled("salt"));
    }
}
