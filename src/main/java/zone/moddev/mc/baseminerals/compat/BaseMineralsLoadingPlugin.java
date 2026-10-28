package zone.moddev.mc.baseminerals.compat;

import java.util.Map;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;

/** Relinks old add-on names without installing a second Base Metals transformer. */
@IFMLLoadingPlugin.Name("BaseMinerals")
@IFMLLoadingPlugin.MCVersion("1.10.2")
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
