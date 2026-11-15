package zone.moddev.mc.baseminerals;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.InterModEnqueueEvent;
import zone.moddev.mc.baseminerals.content.ModContent;
import zone.moddev.mc.baseminerals.worldgen.BaseMineralsWorldgenProvider;

@Mod(BaseMinerals.MOD_ID)
public final class BaseMinerals {
    public static final String MOD_ID = "baseminerals";

    public BaseMinerals(IEventBus modBus) {
        ModContent.register(modBus);
        modBus.addListener(InterModEnqueueEvent.class, event -> BaseMineralsWorldgenProvider.enqueue());
    }
}
