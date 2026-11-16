package zone.moddev.mc.baseminerals.worldgen;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;

import com.google.gson.JsonObject;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import zone.moddev.mc.baseminerals.BaseMinerals;
import zone.moddev.mc.orespawn.api.GeologyFamily;
import zone.moddev.mc.orespawn.api.OreDimensionSelector;
import zone.moddev.mc.orespawn.api.OreHeightDistribution;
import zone.moddev.mc.orespawn.api.OrePattern;
import zone.moddev.mc.orespawn.api.OreSpawnApi;
import zone.moddev.mc.orespawn.api.WorldgenProvider;

/** Declarations only: OreSpawn resolves, bakes and executes all placement. */
public final class BaseMineralsWorldgenProvider {
    /** Hosts every rule accepts, as well as its geology families. */
    static final List<String> STONE_HOSTS = List.of("stone", "granite", "diorite", "andesite",
            "polished_granite", "polished_diorite", "polished_andesite");

    static final List<Ore> ORES = List.of(
            new Ore("lithium", 8, 47, 4, 12, 18, OrePattern.VEIN, OreHeightDistribution.TRIANGLE,
                    8, 4, 3, 8, GeologyFamily.IGNEOUS_INTRUSIVE),
            new Ore("niter", 16, 63, 5, 8, 16, OrePattern.CLUSTERS, OreHeightDistribution.UNIFORM,
                    4, 2, 3, 8, GeologyFamily.SEDIMENTARY),
            new Ore("salt", 24, 79, 2.5, 16, 32, OrePattern.CLUSTERS, OreHeightDistribution.UNIFORM,
                    8, 0, 8, 8, GeologyFamily.SEDIMENTARY),
            new Ore("sulfur", 4, 31, 3, 12, 18, OrePattern.CLUSTERS, OreHeightDistribution.BOTTOM_TRIANGLE,
                    4, 2, 4, 8, GeologyFamily.SEDIMENTARY, GeologyFamily.IGNEOUS_VOLCANIC),
            new Ore("phosphorus", 16, 63, 3, 12, 18, OrePattern.CLUSTERS, OreHeightDistribution.TRIANGLE,
                    6, 1, 6, 8, GeologyFamily.SEDIMENTARY),
            new Ore("potash", 8, 47, 2.5, 12, 24, OrePattern.CLUSTERS, OreHeightDistribution.UNIFORM,
                    6, 0, 6, 8, GeologyFamily.SEDIMENTARY),
            new Ore("saltpeter", 24, 79, 3.75, 8, 16, OrePattern.CLUSTERS, OreHeightDistribution.UNIFORM,
                    4, 1, 3, 8, GeologyFamily.SEDIMENTARY));

    private static final String[] GEOMES = {"stable_craton", "mountain_belt", "volcanic_arc",
            "sedimentary_basin", "coastal_shelf", "arid_basin", "wetland_basin", "glacial_highland"};
    private static final Map<String, Tuning> OVERWORLD = Map.of(
            "lithium", new Tuning(-32, 56, 3, 15, new double[]{1.4, 1.2, .45, .2, .1, .45, .1, .7}),
            "niter", new Tuning(32, 88, 1.5, 12, new double[]{.2, .1, .15, .7, .15, 2, .02, .05}),
            "salt", new Tuning(16, 80, .55, 20, new double[]{.03, .03, .02, .5, .08, 2, .01, .03}),
            "sulfur", new Tuning(-32, 64, 1.5, 15, new double[]{.15, .3, 2.5, .7, .65, 1.5, .15, .2}),
            "phosphorus", new Tuning(-16, 64, 1.5, 15, new double[]{.35, .2, .2, 1.5, 1.5, .7, .7, .35}),
            "potash", new Tuning(8, 64, .8, 18, new double[]{.05, .05, .03, .7, .12, 1.8, .02, .05}),
            "saltpeter", new Tuning(40, 96, 1.25, 12, new double[]{.2, .1, .15, .65, .15, 2, .02, .05}));

    private BaseMineralsWorldgenProvider() {}

    /** Hands the registered ores to OreSpawn; runs during {@code InterModEnqueueEvent}. */
    public static void enqueue() {
        build(BuiltInRegistries.BLOCK::containsKey).ifPresent(provider -> {
            if (!OreSpawnApi.enqueue(provider)) {
                throw new IllegalStateException("OreSpawn did not accept the Base Minerals worldgen provider");
            }
        });
    }

    /**
     * The provider for every ore whose block {@code available} accepts, or empty when
     * none is.
     */
    public static Optional<WorldgenProvider> build(Predicate<ResourceLocation> available) {
        List<WorldgenProvider.OreDefinition> ores = new ArrayList<>();
        for (Ore rule : ORES) {
            ResourceLocation block = id(BaseMinerals.MOD_ID, rule.name + "_ore");
            if (!available.test(block)) continue;
            // Rule IDs and materials are kept from 1.13.2, so saved OreSpawn profiles still apply.
            WorldgenProvider.OreDefinition.Builder ore = WorldgenProvider.OreDefinition
                    .builder(id(BaseMinerals.MOD_ID, "legacy/" + rule.name + "_ore"), block)
                    .material(id("orespawn", rule.name)).nativeGeneration(false).retrogen(false)
                    .output(block, 100D);
            // The explicit Overworld rule overrides the ordinary-dimension fallback,
            // so each chunk receives a single placement budget.
            ore.dimension(id("minecraft", "overworld"), rule::configureOverworld);
            ore.dimensionSelector(OreDimensionSelector.ALL_EXCEPT_NETHER_AND_END, rule::configure);
            ores.add(ore.build());
        }
        if (ores.isEmpty()) return Optional.empty();
        WorldgenProvider.Builder provider = WorldgenProvider.builder(BaseMinerals.MOD_ID, 2);
        ores.forEach(provider::ore);
        return Optional.of(provider.build());
    }

    private static ResourceLocation id(String namespace, String path) {
        return ResourceLocation.fromNamespaceAndPath(namespace, path);
    }

    private record Tuning(int minY, int maxY, double frequency, int quantity, double[] geomes) {}

    /**
     * One mineral's placement. Ranges are inclusive; frequency is expected attempts
     * per chunk; quantity is a requested block budget.
     */
    record Ore(String name, int minY, int maxY, double frequency, int minQuantity, int maxQuantity,
            OrePattern pattern, OreHeightDistribution height, int horizontal, int vertical, int node,
            int length, GeologyFamily... families) {
        void configure(WorldgenProvider.OreDimensionDefinition.Builder placement) {
            placement.yRange(minY, maxY).attempts(frequency).quantityRange(minQuantity, maxQuantity)
                    .pattern(pattern).heightDistribution(height).placementChannel(id("orespawn", "standard"))
                    .spread(horizontal, vertical).nodeSize(node);
            for (String host : STONE_HOSTS) placement.hostBlock(id("minecraft", host));
            // API major 1 exposes vein length through the standard pattern codec.
            if (pattern == OrePattern.VEIN) {
                JsonObject settings = new JsonObject();
                settings.addProperty("length", length);
                settings.addProperty("node_size", node);
                settings.addProperty("spread", horizontal);
                settings.addProperty("vertical_spread", vertical);
                placement.pattern(id("orespawn", "vein"), settings);
            }
            for (GeologyFamily family : families) placement.hostFamily(family);
        }

        void configureOverworld(WorldgenProvider.OreDimensionDefinition.Builder placement) {
            Tuning tuning = OVERWORLD.get(name);
            placement.yRange(tuning.minY, tuning.maxY).attempts(tuning.frequency).quantity(tuning.quantity)
                    .pattern(pattern).heightDistribution(height).placementChannel(id("orespawn", "standard"))
                    .spread(horizontal, vertical).nodeSize(node)
                    .hostBlock(id("minecraft", "stone")).hostBlock(id("minecraft", "deepslate"));
            if (pattern == OrePattern.VEIN) {
                JsonObject settings = new JsonObject();
                settings.addProperty("length", length);
                settings.addProperty("node_size", node);
                settings.addProperty("spread", horizontal);
                settings.addProperty("vertical_spread", vertical);
                placement.pattern(id("orespawn", "vein"), settings);
            }
            for (GeologyFamily family : families) placement.hostFamily(family);
            for (int i = 0; i < GEOMES.length; i++) placement.geomeWeight(id("orespawn", GEOMES[i]), tuning.geomes[i]);
        }
    }
}
