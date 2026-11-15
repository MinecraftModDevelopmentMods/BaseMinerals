package zone.moddev.mc.baseminerals;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import net.minecraft.resources.ResourceLocation;

/**
 * The block, item and fluid IDs that Base Minerals 2.5.1 registered on 1.13.2. Worlds
 * saved then refer to these names, so the port must keep every one. Written out
 * rather than derived from the production code, so a rename fails the tests.
 */
public final class LegacyIds {
    public static final List<String> MINERALS =
            List.of("lithium", "niter", "phosphorus", "potash", "salt", "saltpeter", "silicon", "sulfur");
    public static final List<String> ORES =
            List.of("lithium", "niter", "phosphorus", "potash", "salt", "saltpeter", "sulfur");
    public static final List<String> MOLTEN = List.of("lithium", "silicon");

    private LegacyIds() {}

    public static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(BaseMinerals.MOD_ID, path);
    }

    /** Mineral storage blocks and ores; each drops itself. */
    public static List<ResourceLocation> solidBlocks() {
        List<ResourceLocation> ids = new ArrayList<>();
        MINERALS.forEach(name -> ids.add(id(name + "_block")));
        ORES.forEach(name -> ids.add(id(name + "_ore")));
        return ids;
    }

    /** Every block, including the two molten liquid blocks. */
    public static List<ResourceLocation> blocks() {
        return Stream.concat(solidBlocks().stream(), MOLTEN.stream().map(LegacyIds::id)).toList();
    }

    /** Every item: one per block, then the powders, ingots, nuggets, buckets and blends. */
    public static List<ResourceLocation> items() {
        List<ResourceLocation> ids = new ArrayList<>(blocks());
        for (String name : MINERALS) {
            ids.add(id(name + "_powder"));
            ids.add(id(name + "_smallpowder"));
        }
        for (String name : MOLTEN) {
            ids.add(id(name + "_ingot"));
            ids.add(id(name + "_nugget"));
            ids.add(id(name + "_bucket"));
        }
        ids.add(id("silicon_blend"));
        ids.add(id("silicon_smallblend"));
        return ids;
    }

    /** Source and flowing fluids for each molten mineral. */
    public static List<ResourceLocation> fluids() {
        return MOLTEN.stream().flatMap(name -> Stream.of(id(name), id("flowing_" + name))).toList();
    }
}
