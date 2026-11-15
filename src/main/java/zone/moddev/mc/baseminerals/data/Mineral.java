package zone.moddev.mc.baseminerals.data;

import java.util.List;
import java.util.Objects;

import net.minecraft.resources.ResourceLocation;
import zone.moddev.mc.baseminerals.BaseMinerals;
import zone.moddev.mc.lib.MMDLib;
import zone.moddev.mc.lib.common.factories.MaterialFactory;
import zone.moddev.mc.lib.common.types.MMDMaterial;

/**
 * One of the eight minerals. Each is an MMDLib material in {@code mmdlib/material/},
 * which names its forms; this list says which minerals have an ore and which melt,
 * so code that runs before MMDLib loads can rely on it.
 *
 * @param name   the material name and ID prefix, such as {@code lithium}
 * @param ore    whether the mineral has an ore block
 * @param molten whether the mineral has a molten fluid, ingot and nugget
 */
public record Mineral(String name, boolean ore, boolean molten) {
    public static final List<Mineral> ALL = List.of(
            new Mineral("lithium", true, true),
            new Mineral("niter", true, false),
            new Mineral("phosphorus", true, false),
            new Mineral("potash", true, false),
            new Mineral("salt", true, false),
            new Mineral("saltpeter", true, false),
            new Mineral("silicon", false, true),
            new Mineral("sulfur", true, false));

    public Mineral {
        Objects.requireNonNull(name, "name");
    }

    /** The minerals that have a molten fluid. */
    public static List<Mineral> meltable() {
        return ALL.stream().filter(Mineral::molten).toList();
    }

    /** The material ID, which is also the molten fluid's ID. */
    public ResourceLocation id() {
        return ResourceLocation.fromNamespaceAndPath(BaseMinerals.MOD_ID, name);
    }

    /** The ID of this mineral's {@code form}, such as {@code baseminerals:lithium_powder}. */
    public ResourceLocation id(String form) {
        return ResourceLocation.fromNamespaceAndPath(BaseMinerals.MOD_ID, name + "_" + form);
    }

    /** The mineral's MMDLib material; available once MMDLib has loaded definitions. */
    public MMDMaterial material() {
        MaterialFactory.Result result = MMDLib.loadedMaterials().get(id());
        if (result == null) {
            throw new IllegalStateException("MMDLib has not loaded the material " + id());
        }
        return result.material();
    }

    /** The material's ARGB colour, which tints the molten fluid. */
    public int colour() {
        return material().properties().color();
    }
}
