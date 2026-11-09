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
            java.lang.reflect.Field field=com.mcmoddev.lib.util.Config.Options.class.getDeclaredField(key);
            field.setAccessible(true);((java.util.Map<?,?>)field.get(null)).clear();
        }
    }
    @Test void initializesMaterialAndFluidSwitchesTogether() {
        Configuration config=new Configuration();
        BMiConfig.load(config);
        for(String name:new String[]{"lithium","silicon"}) {
            assertTrue(BMiConfig.isEnabled(name));
            assertTrue(com.mcmoddev.lib.util.Config.Options.isFluidEnabled(name));
        }
        assertTrue(config.getCategory(BMiConfig.MATERIALS).get("EnableLithium").requiresMcRestart());
    }
    @Test void disabledMaterialAlsoDisablesItsFluidAtStartup() {
        Configuration config=new Configuration();
        config.get("Metals","EnableLithium",true).set(false);
        BMiConfig.load(config);
        assertFalse(BMiConfig.isEnabled("lithium"));
        assertFalse(com.mcmoddev.lib.util.Config.Options.isFluidEnabled("lithium"));
    }
    @Test void charcoalSwitchDoesNotDisableBaseMetalsSharedMaterial() {
        com.mcmoddev.lib.util.Config.Options.materialEnabled("charcoal",true);
        Configuration config=new Configuration();
        config.get("Vanilla","EnableCharcoal",true).set(false);
        BMiConfig.load(config);
        assertFalse(BMiConfig.isEnabled("charcoal"));
        assertTrue(com.mcmoddev.lib.util.Config.Options.isMaterialEnabled("charcoal"));
    }
    @Test void historicalFluidSwitchDoesNotDisableTheMaterial() {
        Configuration config = new Configuration();
        config.get(BMiConfig.FLUIDS, "Enabled Lithium", true).set(false);
        BMiConfig.load(config);
        assertTrue(BMiConfig.isEnabled("lithium"));
        assertFalse(com.mcmoddev.lib.util.Config.Options.isFluidEnabled("lithium"));
    }
    @Test void editingRequestedSwitchDoesNotChangeActiveRegistration() {
        Configuration config=new Configuration();
        BMiConfig.load(config);
        config.get("Metals","EnableSalt",true).set(false);
        assertTrue(BMiConfig.isEnabled("salt"));
    }
}
