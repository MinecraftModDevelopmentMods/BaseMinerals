package zone.moddev.mc.baseminerals.recipe;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.RecipeSerializers;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.ModList;
import zone.moddev.mc.baseminerals.BaseMinerals;

/** Selects a crushing type when data packs load. The selected type handles recipe sync. */
public final class SharedCrushingSerializer implements IRecipeSerializer<IRecipe> {
    private static final ResourceLocation NAME = new ResourceLocation(BaseMinerals.MOD_ID, "shared_crushing");

    @Override
    public IRecipe read(ResourceLocation id, JsonObject json) {
        ModList mods = ModList.get();
        String host;
        if (mods.isLoaded("basemetals")) {
            host = "basemetals";
        } else if (mods.isLoaded("modernmetals")) {
            host = "modernmetals";
        } else {
            throw new JsonSyntaxException(NAME + " requires Base Metals or Modern Metals");
        }
        JsonObject shared = new JsonObject();
        shared.addProperty("type", host + ":crushing");
        shared.add("ingredient", json.get("ingredient"));
        shared.add("result", json.get("result"));
        return RecipeSerializers.deserialize(id, shared);
    }

    @Override
    public IRecipe read(ResourceLocation id, PacketBuffer buffer) {
        throw new UnsupportedOperationException("Shared crushing uses the selected mod's serializer for sync");
    }

    @Override
    public void write(PacketBuffer buffer, IRecipe recipe) {
        throw new UnsupportedOperationException("Shared crushing uses the selected mod's serializer for sync");
    }

    @Override
    public ResourceLocation getName() {
        return NAME;
    }
}
