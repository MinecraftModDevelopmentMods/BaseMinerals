package zone.moddev.mc.baseminerals.compat;

import java.util.Map;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;

/** Relinks add-on class names and leaves MMDLib's shared API in its own jar. */
@IFMLLoadingPlugin.Name("BaseMinerals")
@IFMLLoadingPlugin.MCVersion("1.12.2")
@IFMLLoadingPlugin.SortingIndex(1002)
@IFMLLoadingPlugin.TransformerExclusions("zone.moddev.mc.baseminerals.compat.")
public final class BaseMineralsLoadingPlugin implements IFMLLoadingPlugin {
    @Override public String[] getASMTransformerClass() {
        return new String[] { "zone.moddev.mc.baseminerals.compat.LegacyNamespaceTransformer" };
    }
    @Override public String getModContainerClass() { return null; }
    @Override public String getSetupClass() { return null; }
    @Override public void injectData(final Map<String, Object> data) { }
    @Override public String getAccessTransformerClass() { return null; }
}
