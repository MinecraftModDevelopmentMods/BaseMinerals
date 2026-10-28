package zone.moddev.mc.baseminerals.worldgen;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.Consumer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import zone.moddev.mc.orespawn.api.*;

/** Declarations only: OreSpawn resolves, bakes and executes all placement. */
public final class BaseMineralsWorldgenProvider {
    private BaseMineralsWorldgenProvider() { }
    private static final Ore[] ORES={
        new Ore("lithium",8,47,4,12,18,OrePattern.VEIN,OreHeightDistribution.TRIANGLE,8,4,3,8,GeologyFamily.IGNEOUS_INTRUSIVE),
        new Ore("niter",16,63,5,8,16,OrePattern.CLUSTERS,OreHeightDistribution.UNIFORM,4,2,3,8,GeologyFamily.SEDIMENTARY),
        new Ore("salt",24,79,2.5,16,32,OrePattern.CLUSTERS,OreHeightDistribution.UNIFORM,8,0,8,8,GeologyFamily.SEDIMENTARY),
        new Ore("sulfur",4,31,3,12,18,OrePattern.CLUSTERS,OreHeightDistribution.BOTTOM_TRIANGLE,4,2,4,8,GeologyFamily.SEDIMENTARY,GeologyFamily.IGNEOUS_VOLCANIC),
        new Ore("phosphorus",16,63,3,12,18,OrePattern.CLUSTERS,OreHeightDistribution.TRIANGLE,6,1,6,8,GeologyFamily.SEDIMENTARY),
        new Ore("potash",8,47,2.5,12,24,OrePattern.CLUSTERS,OreHeightDistribution.UNIFORM,6,0,6,8,GeologyFamily.SEDIMENTARY),
        new Ore("saltpeter",24,79,3.75,8,16,OrePattern.CLUSTERS,OreHeightDistribution.UNIFORM,4,1,3,8,GeologyFamily.SEDIMENTARY)
    };
    public static void enqueue() {
        build(ForgeRegistries.BLOCKS::containsKey).ifPresent(provider->{
            if(!OreSpawnApi.enqueue(provider)) throw new IllegalStateException("Base Minerals provider registered twice");
        });
    }
    public static Optional<WorldgenProvider> build(Predicate<ResourceLocation> available) {
        List<WorldgenProvider.OreDefinition> ores=new ArrayList<>();
        for(Ore rule:ORES) {
            ResourceLocation block=id("baseminerals",rule.name+"_ore");
            if(!available.test(block)) continue;
            WorldgenProvider.OreDefinition.Builder ore=WorldgenProvider.OreDefinition.builder(id("baseminerals","legacy/"+rule.name+"_ore"),block)
                .material(id("orespawn",rule.name)).nativeGeneration(false).retrogen(false).output(block,100D);
            Consumer<WorldgenProvider.OreDimensionDefinition.Builder> placement=p->{
                p.yRange(rule.minY,rule.maxY).attempts(rule.frequency).quantityRange(rule.minQuantity,rule.maxQuantity)
                    .pattern(rule.pattern).heightDistribution(rule.height).placementChannel(id("orespawn","standard"))
                    .spread(rule.horizontal,rule.vertical).nodeSize(rule.node).hostBlock(id("minecraft","stone"));
                // API major 1 exposes vein length through the standard pattern codec.
                if(rule.pattern==OrePattern.VEIN) {
                    com.google.gson.JsonObject settings=new com.google.gson.JsonObject();
                    settings.addProperty("length",rule.length);
                    settings.addProperty("node_size",rule.node);
                    settings.addProperty("spread",rule.horizontal);
                    settings.addProperty("vertical_spread",rule.vertical);
                    p.pattern(id("orespawn","vein"),settings);
                }
                for(GeologyFamily family:rule.families) p.hostFamily(family);
            };
            // Exact-domain siblings participate in the same Overworld source group.
            // The explicit rule overrides this fallback, so placement still runs once.
            ore.dimension(id("minecraft","overworld"),placement);
            ore.dimensionSelector(OreDimensionSelector.ALL_EXCEPT_NETHER_AND_END,placement);
            ores.add(ore.build());
        }
        if(ores.isEmpty())return Optional.empty();
        WorldgenProvider.Builder provider=WorldgenProvider.builder("baseminerals",1);
        ores.forEach(provider::ore);
        return Optional.of(provider.build());
    }
    private static ResourceLocation id(String mod,String name){return new ResourceLocation(mod,name);}
    private static final class Ore {
        final String name; final int minY,maxY,minQuantity,maxQuantity,horizontal,vertical,node,length;
        final double frequency; final OrePattern pattern; final OreHeightDistribution height; final GeologyFamily[] families;
        Ore(String name,int minY,int maxY,double frequency,int minQuantity,int maxQuantity,OrePattern pattern,OreHeightDistribution height,int horizontal,int vertical,int node,int length,GeologyFamily...families) {
            this.name=name;this.minY=minY;this.maxY=maxY;this.frequency=frequency;this.minQuantity=minQuantity;this.maxQuantity=maxQuantity;
            this.pattern=pattern;this.height=height;this.horizontal=horizontal;this.vertical=vertical;this.node=node;this.length=length;this.families=families;
        }
    }
}
