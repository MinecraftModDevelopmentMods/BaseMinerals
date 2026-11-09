package review.minerals;

import com.google.gson.*;
import java.util.*;
import net.minecraft.util.ResourceLocation;
import zone.moddev.mc.orespawn.api.*;

/** Test-only counting wrapper; delegates placement to the published built-in codecs. */
final class RecordingPatterns {
    private static final Map<String,Stats> COUNTS=new LinkedHashMap<>();
    private static final Map<String,JsonObject> SETTINGS=new LinkedHashMap<>();
    static void register() {
        for(String name:BaseMineralsProbe.ORES) {
            OrePatternType type=OrePatternType.create(StandardPatternSettings.CODEC,settings->{
                JsonObject json=new JsonObject();json.addProperty("spread",settings.spread());json.addProperty("vertical_spread",settings.verticalSpread());
                json.addProperty("node_size",settings.nodeSize());json.addProperty("length",settings.length());
                SETTINGS.put(name,new JsonParser().parse(json.toString()).getAsJsonObject());
                CompiledOrePattern delegate=OreSpawnPatternRegistry.registry().getValue(new ResourceLocation("orespawn","lithium".equals(name)?"vein":"clusters")).decode(json);
                return context->{
                    int cx=Math.floorDiv(context.originX(),16),cz=Math.floorDiv(context.originZ(),16);
                    if(cx>=64&&cx<80&&cz>=64&&cz<80) {
                        Stats stats=COUNTS.computeIfAbsent(name,key->new Stats());stats.attempts++;stats.heights[context.originY()]++;
                        stats.min=Math.min(stats.min,context.quantity());stats.max=Math.max(stats.max,context.quantity());
                    }
                    return delegate.place(context);
                };
            });
            type.setRegistryName(new ResourceLocation("basemineralsprobe",name));OreSpawnPatternRegistry.registry().register(type);
        }
    }
    static JsonObject report(JsonArray failures) {
        JsonObject result=new JsonObject();
        for(Map.Entry<String,Stats> entry:COUNTS.entrySet()) {
            Stats stats=entry.getValue();JsonObject value=new JsonObject();value.addProperty("attempts",stats.attempts);value.addProperty("minQuantity",stats.min);value.addProperty("maxQuantity",stats.max);
            JsonArray heights=new JsonArray();for(int n:stats.heights)heights.add(new JsonPrimitive(n));value.add("heights",heights);result.add(entry.getKey(),value);
            value.add("settings",SETTINGS.get(entry.getKey()));
        }
        return result;
    }
    private static final class Stats {int attempts,min=Integer.MAX_VALUE,max;final int[] heights=new int[256];}
}
