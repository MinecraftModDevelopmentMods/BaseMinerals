package zone.moddev.mc.baseminerals.init;

import com.google.gson.JsonObject;
import java.util.function.BooleanSupplier;
import net.minecraft.util.JsonUtils;
import net.minecraftforge.common.crafting.IConditionFactory;
import net.minecraftforge.common.crafting.JsonContext;
import zone.moddev.mc.baseminerals.util.BMiConfig;

/** Uses the add-on's latched switches without changing another mod's materials. */
public final class MaterialEnabledCondition implements IConditionFactory {
    @Override
    public BooleanSupplier parse(JsonContext context, JsonObject condition) {
        String material = JsonUtils.getString(condition, "material");
        return () -> BMiConfig.isEnabled(material);
    }
}
