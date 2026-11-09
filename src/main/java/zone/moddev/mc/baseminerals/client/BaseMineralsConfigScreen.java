package zone.moddev.mc.baseminerals.client;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.IConfigElement;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import zone.moddev.mc.baseminerals.BaseMinerals;
import zone.moddev.mc.baseminerals.util.BMiConfig;

/** One parent-aware screen for Forge's mod list and OreSpawn's child-mod list. */
@SideOnly(Side.CLIENT)
public final class BaseMineralsConfigScreen extends GuiConfig {
    public BaseMineralsConfigScreen(final GuiScreen parent) {
        super(parent, elements(),
              BaseMinerals.MODID, false, true, I18n.format("config.baseminerals.title"),
              I18n.format("config.baseminerals.description"));
    }
    private static List<IConfigElement> elements() {
        final List<IConfigElement> elements = new ArrayList<>(
                new ConfigElement(BMiConfig.getConfiguration().getCategory(BMiConfig.MATERIALS)).getChildElements());
        elements.addAll(new ConfigElement(BMiConfig.getConfiguration().getCategory(BMiConfig.FLUIDS)).getChildElements());
        elements.addAll(new ConfigElement(BMiConfig.getConfiguration().getCategory(BMiConfig.VANILLA)).getChildElements());
        return elements;
    }
}
