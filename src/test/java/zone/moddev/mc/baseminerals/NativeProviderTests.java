package zone.moddev.mc.baseminerals;

import static org.junit.jupiter.api.Assertions.*;
import com.google.gson.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.Optional;
import java.util.function.Predicate;
import net.minecraft.util.ResourceLocation;
import org.junit.jupiter.api.Test;
import zone.moddev.mc.orespawn.api.WorldgenProvider;

class NativeProviderTests {
    @SuppressWarnings("unchecked")
    private Optional<WorldgenProvider> build(Predicate<ResourceLocation> available) throws Exception {
        return (Optional<WorldgenProvider>) Class.forName("zone.moddev.mc.baseminerals.worldgen.BaseMineralsWorldgenProvider")
            .getMethod("build", Predicate.class).invoke(null, available);
    }
    @Test void implementsApprovedBalanceAndExportsSchema() throws Exception {
        JsonObject provider = build(id -> true).get().toJson();
        Files.createDirectories(Paths.get("build/reports"));
        Files.write(Paths.get("build/reports/native-provider.json"), (provider.toString()+"\n").getBytes(StandardCharsets.UTF_8));
        assertEquals(5,provider.get("schema_version").getAsInt());
        JsonObject ores=provider.getAsJsonObject("ores");
        assertEquals(7,ores.entrySet().size());
        String[] names={"lithium","niter","salt","sulfur","phosphorus","potash","saltpeter"};
        double[][] rules={{8,47,4,12,18},{16,63,5,8,16},{24,79,2.5,16,32},{4,31,3,12,18},{16,63,3,12,18},{8,47,2.5,12,24},{24,79,3.75,8,16}};
        int[][] shapes={{8,4,3},{4,2,3},{8,0,8},{4,2,4},{6,1,6},{6,0,6},{4,1,3}};
        String[] heights={"triangle","uniform","uniform","bottom_triangle","triangle","uniform","uniform"};
        for(int i=0;i<names.length;i++) {
            JsonObject ore=ores.getAsJsonObject("baseminerals:legacy/"+names[i]+"_ore");
            assertEquals("orespawn:"+names[i],ore.get("material").getAsString());
            assertFalse(ore.get("retrogen").getAsBoolean());
            assertFalse(ore.get("native_generation").getAsBoolean());
            JsonObject p=ore.getAsJsonObject("dimension_selectors").getAsJsonObject("orespawn:all_except_nether_end");
            String[] fields={"min_y","max_y","frequency","min_quantity","max_quantity"};
            for(int j=0;j<fields.length;j++) assertEquals(rules[i][j],p.get(fields[j]).getAsDouble(),names[i]+":"+fields[j]);
            assertEquals("orespawn:standard",p.get("placement_channel").getAsString());
            assertTrue(p.getAsJsonArray("host_blocks").toString().contains("minecraft:stone"));
            assertEquals(p,ore.getAsJsonObject("dimensions").getAsJsonObject("minecraft:overworld"));
            assertEquals(heights[i],p.get("height_distribution").getAsString());
            String[] shapeFields={"spread","vertical_spread","node_size"};
            for(int j=0;j<shapeFields.length;j++)assertEquals(shapes[i][j],p.get(shapeFields[j]).getAsInt());
            if(i==0) {
                assertEquals("orespawn:vein",p.getAsJsonObject("pattern").get("type").getAsString());
                assertEquals(8,p.getAsJsonObject("pattern").getAsJsonObject("settings").get("length").getAsInt());
            } else assertEquals("clusters",p.get("pattern").getAsString());
            assertTrue(p.getAsJsonArray("host_families").toString().contains(i==0?"igneous_intrusive":"sedimentary"));
            if(i==3)assertTrue(p.getAsJsonArray("host_families").toString().contains("igneous_volcanic"));
            double historicalBudget=(i<3?8:6)*7.5;
            assertEquals(historicalBudget,rules[i][2]*(rules[i][3]+rules[i][4])/2);
            assertEquals(0,p.getAsJsonArray("biome_ids").size());
        }
        for(String section:new String[]{"rocks","terrain_dimensions","geomes","fluid_deposits","biome_palettes","dimension_materials"})
            assertEquals(0,provider.getAsJsonObject(section).entrySet().size(),section);
    }
    @Test void absentBlocksCannotLeaveActiveRules() throws Exception {
        assertFalse(build(id -> false).isPresent());
        JsonObject ores=build(id -> !id.getResourcePath().equals("lithium_ore")).get().toJson().getAsJsonObject("ores");
        assertEquals(6,ores.entrySet().size());
        assertFalse(ores.has("baseminerals:legacy/lithium_ore"));
    }
}
