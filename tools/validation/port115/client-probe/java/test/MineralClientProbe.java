package test;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.MainMenuScreen;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.GameType;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.WorldType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

/** Explicit client gate. Normal launches and published jars exclude this mod. */
@Mod("mineralclientprobe")
@Mod.EventBusSubscriber(modid = "mineralclientprobe", value = Dist.CLIENT)
public final class MineralClientProbe {
    private static int stage, ticks, loaded, checks, items;
    private static net.minecraft.server.MinecraftServer stopping;
    private static final JsonObject heldMining = new JsonObject();
    private static final List<String[]> heldCases = new ArrayList<>();
    private static int digIndex, digPhase, digTicks, attackTicks, allowedAttackTicks;
    private static java.util.concurrent.CompletableFuture<Void> operation;
    private static final net.minecraft.util.math.BlockPos DIG_POS = new net.minecraft.util.math.BlockPos(128, 180, 128);

    private static void check(boolean value, String message) {
        checks++;
        if (!value) throw new IllegalStateException(message);
    }

    @SubscribeEvent
    public static void tick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END || stage == 5) return;
        Minecraft mc = Minecraft.getInstance();
        try {
            if (mc.currentScreen instanceof net.minecraftforge.fml.client.gui.screen.LoadingErrorScreen)
                throw new IllegalStateException("Forge reported a loading error");
            if (++ticks > 3600) throw new IllegalStateException("Client timeout at stage " + stage);
            if (stage == 0 && mc.currentScreen instanceof MainMenuScreen) {
                validate(mc);
                mc.launchIntegratedServer("mineral-smoke", "Base Minerals 1.15 check",
                        new WorldSettings(8675309L, GameType.SURVIVAL, false, false, WorldType.DEFAULT));
                stage = 1; ticks = 0;
            } else if (stage == 1 && mc.world != null && mc.player != null && ++loaded > 80) {
                if (!exerciseHeldMining(mc)) return;
                stopping = mc.getIntegratedServer();
                mc.world.sendQuittingDisconnectingPacket();
                mc.loadWorld(null);
                mc.displayGuiScreen(new MainMenuScreen());
                stage = 2; ticks = 0; loaded = 0;
            } else if (stage == 2 && mc.currentScreen instanceof MainMenuScreen && !stopping.getExecutionThread().isAlive()) {
                mc.launchIntegratedServer("mineral-smoke", "Base Minerals 1.15 check", null);
                stage = 3; ticks = 0;
            } else if (stage == 3 && mc.world != null && mc.player != null && ++loaded > 80) {
                stopping = mc.getIntegratedServer();
                mc.world.sendQuittingDisconnectingPacket();
                mc.loadWorld(null);
                mc.displayGuiScreen(new MainMenuScreen());
                stage = 4; ticks = 0;
            } else if (stage == 4 && !stopping.getExecutionThread().isAlive()) {
                write(true, null); stage = 5; mc.shutdown();
            }
        } catch (Throwable error) {
            error.printStackTrace(); write(false, error.toString()); stage = 5; mc.shutdown();
        }
    }

    private static void validate(Minecraft mc) {
        for (Item item : ForgeRegistries.ITEMS) {
            if (!"baseminerals".equals(item.getRegistryName().getNamespace())) continue;
            ItemStack stack = new ItemStack(item);
            IBakedModel model = mc.getItemRenderer().getItemModelWithOverrides(stack, null, null);
            check(model != mc.getModelManager().getMissingModel(), "Missing model " + item.getRegistryName());
            check(!stack.getDisplayName().getString().startsWith("item.baseminerals.")
                            && !stack.getDisplayName().getString().startsWith("block.baseminerals."),
                    "Missing translation " + item.getRegistryName());
            items++;
        }
        check(items == 41, "The saved item registry changed");
        for (String mineral : new String[] {"lithium", "niter", "phosphorus", "potash", "salt", "saltpeter", "sulfur"}) {
            Block block = ForgeRegistries.BLOCKS.getValue(new ResourceLocation("baseminerals", mineral + "_ore"));
            check(RenderTypeLookup.canRenderInLayer(block.getDefaultState(), RenderType.getCutoutMipped()), "Ore cutout " + mineral);
            oreModel(mc.getBlockRendererDispatcher().getModelForState(block.getDefaultState()), block, mineral);
            oreModel(mc.getItemRenderer().getItemModelWithOverrides(new ItemStack(block), null, null), null, mineral);
        }
        for (String material : new String[] {"lithium", "silicon"}) {
            ItemStack bucket = new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("baseminerals", material + "_bucket")));
            IBakedModel model = mc.getItemRenderer().getItemModelWithOverrides(bucket, null, null);
            List<BakedQuad> quads = new ArrayList<>(model.getQuads(null, null, new Random(1)));
            for (Direction side : Direction.values()) quads.addAll(model.getQuads(null, side, new Random(1)));
            check(quads.stream().anyMatch(q -> q.hasTintIndex() && q.getTintIndex() == 1), "Bucket fluid layer " + material);
            for (BakedQuad quad : quads) check(!"missingno".equals(quad.func_187508_a().getName().getPath()), "Bucket sprite " + material);
            check(mc.getItemColors().getColor(bucket, 1) == 0xC5C8C1, "Bucket colour " + material);
        }
        for (String texture : new String[] {"molten_metal_still", "molten_metal_flow"}) {
            ResourceLocation id = new ResourceLocation("baseminerals", "block/" + texture);
            check(mc.getAtlasSpriteGetter(net.minecraft.client.renderer.texture.AtlasTexture.LOCATION_BLOCKS_TEXTURE).apply(id).getName().equals(id), "Native molten atlas sprite " + texture);
        }
        if (net.minecraftforge.fml.ModList.get().isLoaded("basemetals")) {
            int baseOres = 0;
            for (Block block : ForgeRegistries.BLOCKS) {
                if (!"basemetals".equals(block.getRegistryName().getNamespace()) || !block.getRegistryName().getPath().endsWith("_ore")) continue;
                baseOreGap(mc.getBlockRendererDispatcher().getModelForState(block.getDefaultState()), block);
                baseOreGap(mc.getItemRenderer().getItemModelWithOverrides(new ItemStack(block), null, null), null);
                baseOres++;
            }
            check(baseOres == 13, "All Base Metals ore block/item overlay gaps");
        }
        for (String owner : new String[] {"basemetals", "modernmetals"}) {
            if (!net.minecraftforge.fml.ModList.get().isLoaded(owner)) continue;
            String hammer = owner + ("basemetals".equals(owner) ? ":iron_crackhammer" : ":galvanizedsteel_crackhammer");
            for (String mineral : new String[] {"lithium", "niter", "phosphorus", "potash", "salt", "saltpeter", "sulfur"})
                heldCases.add(new String[] {hammer, "baseminerals:" + mineral + "_ore", "baseminerals:" + mineral + "_powder"});
        }
    }

    private static void baseOreGap(IBakedModel model, Block block) {
        for (Direction side : Direction.values()) {
            List<BakedQuad> quads = model.getQuads(block == null ? null : block.getDefaultState(), side, new Random(42));
            check(quads.size() == 2, "Base ore layers on " + side);
            boolean sawOverlay = false;
            for (BakedQuad quad : quads) {
                boolean overlay = quad.func_187508_a().getName().getPath().startsWith("block/ore_overlays/");
                sawOverlay |= overlay;
                float low = overlay ? -0.05F / 16 : 0, high = overlay ? 16.05F / 16 : 1;
                int[] data = quad.getVertexData(); int stride = data.length / 4;
                for (int vertex = 0; vertex < 4; vertex++) {
                    float u = (Float.intBitsToFloat(data[vertex * stride + 4]) - quad.func_187508_a().getMinU()) * 16.0F / (quad.func_187508_a().getMaxU() - quad.func_187508_a().getMinU());
                    float v = (Float.intBitsToFloat(data[vertex * stride + 5]) - quad.func_187508_a().getMinV()) * 16.0F / (quad.func_187508_a().getMaxV() - quad.func_187508_a().getMinV());
                    check(u >= -0.01F && u <= 16.01F && v >= -0.01F && v <= 16.01F, "Base ore sprite UV bounds");
                    for (int axis = 0; axis < 3; axis++) {
                        float coordinate = Float.intBitsToFloat(data[vertex * stride + axis]);
                        check(Math.min(Math.abs(coordinate - low), Math.abs(coordinate - high)) < 0.000001F,
                                "Base ore uses stale overlay spacing: " + quad.func_187508_a().getName() + "=" + coordinate);
                    }
                }
            }
            check(sawOverlay, "Base ore lost overlay");
        }
    }

    /** Runs the same client damage loop as a held left click, including real packets. */
    private static boolean exerciseHeldMining(Minecraft mc) {
        if (digIndex >= heldCases.size()) return true;
        String[] test = heldCases.get(digIndex);
        if (++digTicks > 180) throw new IllegalStateException("Held mining timeout " + java.util.Arrays.toString(test) + " phase=" + digPhase);
        if (digPhase == 0) {
            mc.playerController.resetBlockRemoving();
            operation = mc.getIntegratedServer().runAsync(() -> {
                net.minecraft.world.server.ServerWorld world = mc.getIntegratedServer().getWorld(net.minecraft.world.dimension.DimensionType.OVERWORLD);
                net.minecraft.entity.player.ServerPlayerEntity player = mc.getIntegratedServer().getPlayerList().getPlayerByUUID(mc.player.getUniqueID());
                player.interactionManager.setGameType(GameType.SURVIVAL);
                for (int x = -5; x <= 5; x++) for (int z = -5; z <= 5; z++)
                    world.setBlockState(DIG_POS.add(x, -1, z), net.minecraft.block.Blocks.STONE.getDefaultState(), 3);
                player.connection.setPlayerLocation(128.5D, 180.0D, 125.5D, 0, 0);
                world.getEntitiesWithinAABB(net.minecraft.entity.item.ItemEntity.class,
                        new net.minecraft.util.math.AxisAlignedBB(DIG_POS).grow(1.5D)).forEach(net.minecraft.entity.item.ItemEntity::remove);
                player.inventory.currentItem = 0;
                player.inventory.setInventorySlotContents(0, new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation(test[0]))));
                player.sendContainerToPlayer(player.container);
                world.setBlockState(DIG_POS, ForgeRegistries.BLOCKS.getValue(new ResourceLocation(test[1])).getDefaultState(), 3);
            });
            digPhase = 1;
        } else if (digPhase == 1 && operation.isDone()) {
            operation.join(); mc.player.inventory.currentItem = 0;
            if (!test[1].equals(mc.world.getBlockState(DIG_POS).getBlock().getRegistryName().toString())
                    || !test[0].equals(mc.player.getHeldItemMainhand().getItem().getRegistryName().toString())
                    || mc.player.getHeldItemMainhand().getDamage() != 0 || Math.abs(mc.player.getPosY() - 180) > 0.1D) return false;
            net.minecraft.block.BlockState state = mc.world.getBlockState(DIG_POS);
            float speed = mc.player.getHeldItemMainhand().getDestroySpeed(state);
            check(speed > 1.0F, "Client hammer tag did not sync " + test[0] + " -> " + test[1]);
            // The native controller retains five hit-delay ticks between blocks.
            allowedAttackTicks = (int) Math.ceil(state.getBlockHardness(mc.world, DIG_POS) * 30.0F / speed) + 6;
            digPhase = 2; digTicks = 0; attackTicks = 0;
        } else if (digPhase == 2) {
            mc.playerController.onPlayerDamageBlock(DIG_POS, Direction.UP);
            attackTicks++;
            if (mc.world.isAirBlock(DIG_POS)) { digPhase = 3; digTicks = 0; }
            check(attackTicks <= allowedAttackTicks, "Held hammer mining is too slow: " + test[0] + " -> " + test[1]);
        } else if (digPhase == 3 && digTicks > 10) {
            check(mc.world.isAirBlock(DIG_POS), "Mined ore reappeared on the client " + test[1]);
            check(mc.player.getHeldItemMainhand().getDamage() == 1, "Client hammer durability did not sync " + test[1]);
            operation = mc.getIntegratedServer().runAsync(() -> {
                net.minecraft.world.server.ServerWorld world = mc.getIntegratedServer().getWorld(net.minecraft.world.dimension.DimensionType.OVERWORLD);
                check(world.isAirBlock(DIG_POS), "Held mining left the ore on the server " + test[1]);
                int count = 0;
                for (net.minecraft.entity.item.ItemEntity drop : world.getEntitiesWithinAABB(net.minecraft.entity.item.ItemEntity.class,
                        new net.minecraft.util.math.AxisAlignedBB(DIG_POS).grow(1.5D))) {
                    check(test[2].equals(drop.getItem().getItem().getRegistryName().toString()), "Unexpected held mining drop " + test[1]);
                    count += drop.getItem().getCount();
                }
                check(count == 2, "Held mining did not yield two powders " + test[1] + " count=" + count);
            });
            digPhase = 4;
        } else if (digPhase == 4 && operation.isDone()) {
            operation.join(); heldMining.addProperty(test[0] + "/" + test[1], attackTicks);
            digIndex++; digPhase = 0; digTicks = 0;
        }
        return false;
    }

    private static void oreModel(IBakedModel model, Block block, String mineral) {
        check(model.getParticleTexture().getName().toString().equals("minecraft:block/stone"), "Ore particle host " + mineral);
        ResourceLocation overlay = new ResourceLocation("baseminerals", "blocks/ore_overlays/" + mineral + "_ore");
        for (Direction side : Direction.values()) {
            List<BakedQuad> quads = model.getQuads(block == null ? null : block.getDefaultState(), side, new Random(42));
            check(quads.size() == 2, "Ore layers " + mineral + " " + side);
            check(quads.stream().anyMatch(q -> q.func_187508_a().getName().toString().equals("minecraft:block/stone")), "Ore stone layer");
            check(quads.stream().anyMatch(q -> q.func_187508_a().getName().equals(overlay)), "Ore overlay layer");
            for (BakedQuad quad : quads) {
                check(!"missingno".equals(quad.func_187508_a().getName().getPath()), "Missing ore sprite");
                if (!quad.func_187508_a().getName().equals(overlay)) continue;
                boolean transparent = false, visible = false;
                for (int y = 0; y < quad.func_187508_a().getHeight(); y++) for (int x = 0; x < quad.func_187508_a().getWidth(); x++) {
                    if (quad.func_187508_a().isPixelTransparent(0, x, y)) transparent = true;
                    else visible = true;
                }
                check(transparent && visible, "Ore overlay transparency " + mineral);
            }
        }
    }

    private static void write(boolean passed, String failure) {
        JsonObject result = new JsonObject();
        result.addProperty("passed", passed); result.addProperty("checks", checks);
        result.addProperty("items", items); result.addProperty("worldReopened", passed);
        result.add("held_mining_ticks", heldMining);
        if (failure != null) result.addProperty("failure", failure);
        try {
            Files.write(Paths.get("client-probe.json"), new GsonBuilder().setPrettyPrinting().create().toJson(result).getBytes(StandardCharsets.UTF_8));
        } catch (java.io.IOException error) { throw new IllegalStateException(error); }
        System.out.println("MINERAL_CLIENT_PROBE " + (passed ? "PASS" : "FAIL") + " checks=" + checks);
    }
}
