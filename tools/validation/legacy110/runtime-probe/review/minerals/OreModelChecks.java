package review.minerals;

import com.mcmoddev.lib.init.Materials;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.resources.IResource;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.oredict.OreDictionary;
import zone.moddev.mc.baseminerals.util.Config;

import static review.minerals.ClientChecks.require;

/** Checks the texture and geometry of the baked block and inventory models. */
final class OreModelChecks {
    static void verify(Minecraft minecraft) throws IOException {
        int overlays = 0;
        int solids = 0;
        for (String material : BaseMineralsProbe.ORES) {
            if (!Config.isEnabled(material)) continue;
            String name = material + "_ore";
            ResourceLocation id = new ResourceLocation("baseminerals", name);
            Block ore = ForgeRegistries.BLOCKS.getValue(id);
            require(ore != null && id.equals(ore.getRegistryName()), "Wrong ore block ID: " + id);
            Item item = Item.getItemFromBlock(ore);
            require(id.equals(item.getRegistryName()), "Wrong ore item ID: " + id);
            String oreKey = "ore" + Materials.getMaterialByName(material).getCapitalizedName();
            require(OreDictionary.getOres(oreKey).stream().anyMatch(stack -> stack.getItem() == item),
                    "Missing Ore Dictionary entry: " + oreKey);
            require(ore.getBlockLayer() == BlockRenderLayer.CUTOUT_MIPPED, "Wrong ore render layer: " + id);

            String overlay = "baseminerals:blocks/ore_overlays/" + name;
            boolean layered = hasOverlay(minecraft, name);
            String base = layered ? "minecraft:blocks/stone" : "baseminerals:blocks/" + name;
            IBlockState state = ore.getDefaultState();
            IBakedModel placed = minecraft.getBlockRendererDispatcher().getBlockModelShapes().getModelForState(state);
            IBakedModel inventory = minecraft.getRenderItem().getItemModelMesher().getItemModel(new ItemStack(item));
            verifyModel(placed, state, base, layered ? overlay : null);
            verifyModel(inventory, null, base, layered ? overlay : null);
            if (layered) overlays++;
            else solids++;
        }
        System.out.println("BASE_MINERALS_ORE_MODELS_PASS solids=" + solids + " overlays=" + overlays);
    }

    private static boolean hasOverlay(Minecraft minecraft, String name) throws IOException {
        ResourceLocation path = new ResourceLocation("baseminerals", "textures/blocks/ore_overlays/" + name + ".png");
        try (IResource resource = minecraft.getResourceManager().getResource(path)) {
            return true;
        } catch (FileNotFoundException missing) {
            return false;
        }
    }

    private static void verifyModel(IBakedModel model, IBlockState state, String base, String overlay) {
        require(base.equals(model.getParticleTexture().getIconName()), "Wrong ore particles: " + base);
        for (EnumFacing face : EnumFacing.values()) {
            List<BakedQuad> quads = model.getQuads(state, face, 0);
            require(quads.size() == (overlay == null ? 1 : 2), "Wrong ore face count: " + base + " " + face);
            verifyFace(quads, face, base, 0.0F);
            if (overlay != null) verifyFace(quads, face, overlay, 0.05F / 16.0F);
        }
    }

    private static void verifyFace(List<BakedQuad> quads, EnumFacing face, String texture, float offset) {
        BakedQuad quad = quads.stream().filter(candidate -> texture.equals(candidate.getSprite().getIconName()))
                .findFirst().orElseThrow(() -> new AssertionError("Missing ore sprite: " + texture));
        int axis = face.getAxis().ordinal();
        float expected = face.getAxisDirection() == EnumFacing.AxisDirection.POSITIVE ? 1.0F + offset : -offset;
        int[] data = quad.getVertexData();
        for (int vertex = 0; vertex < 4; vertex++) {
            float depth = Float.intBitsToFloat(data[vertex * (data.length / 4) + axis]);
            require(Math.abs(depth - expected) < 0.0001F, "Wrong ore face depth: " + texture + " " + face);
        }
    }
}
