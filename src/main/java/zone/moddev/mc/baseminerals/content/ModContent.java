package zone.moddev.mc.baseminerals.content;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.SoundActions;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.neoforged.neoforge.registries.NewRegistryEvent;
import zone.moddev.mc.baseminerals.BaseMinerals;
import zone.moddev.mc.baseminerals.data.Mineral;
import zone.moddev.mc.lib.MMDLib;
import zone.moddev.mc.lib.api.MaterialExtensions;
import zone.moddev.mc.lib.common.factories.MaterialFactory;
import zone.moddev.mc.lib.common.runtime.RuntimeContentRegistrar;

/**
 * Base Minerals content. The minerals' powders, ingots, nuggets, blends, blocks and
 * ores are MMDLib materials under {@code mmdlib/material/}, registered by MMDLib's
 * runtime registrar. Molten lithium and silicon stay Base Minerals fluids, so their
 * IDs match older saves; the {@code baseminerals:bucket} form fills them.
 */
public final class ModContent {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(Registries.BLOCK, BaseMinerals.MOD_ID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Registries.ITEM, BaseMinerals.MOD_ID);
    public static final DeferredRegister<Fluid> FLUIDS =
            DeferredRegister.create(Registries.FLUID, BaseMinerals.MOD_ID);
    public static final DeferredRegister<FluidType> FLUID_TYPES =
            DeferredRegister.create(NeoForgeRegistries.Keys.FLUID_TYPES, BaseMinerals.MOD_ID);

    private static final Map<String, FluidContent> FLUID_CONTENT = new LinkedHashMap<>();
    private static RuntimeContentRegistrar registrar;

    private ModContent() {}

    public static void register(IEventBus bus) {
        // The bucket form is named by the lithium and silicon materials, so it must
        // exist before MMDLib decodes them in its deferred construction work.
        MineralBucketItem.registerImplementation();
        registrar = new RuntimeContentRegistrar(BaseMinerals.MOD_ID, MaterialExtensions.GLOBAL, bus);
        BLOCKS.register(bus);
        ITEMS.register(bus);
        FLUIDS.register(bus);
        FLUID_TYPES.register(bus);
        bus.addListener(MineralBucketItem::registerCapabilities);
        // MMDLib loads definitions after mod construction; registries open after that.
        bus.addListener(NewRegistryEvent.class, event -> populate());
    }

    private static void populate() {
        for (Mineral mineral : Mineral.ALL) {
            MaterialFactory.Result result = MMDLib.loadedMaterials().get(mineral.id());
            if (result == null) {
                throw new IllegalStateException("MMDLib did not load the Base Minerals material " + mineral.id());
            }
            if (mineral.molten()) registerFluid(mineral);
            registrar.queue(result);
        }
    }

    private static void registerFluid(Mineral mineral) {
        String name = mineral.name();
        ResourceLocation id = mineral.id();
        DeferredHolder<Fluid, BaseFlowingFluid.Source> source = DeferredHolder.create(Registries.FLUID, id);
        DeferredHolder<Fluid, BaseFlowingFluid.Flowing> flowing =
                DeferredHolder.create(Registries.FLUID, id.withPrefix("flowing_"));
        DeferredHolder<Block, MoltenMineralBlock> block = DeferredHolder.create(Registries.BLOCK, id);
        // The baseminerals:bucket item form registers this ID through MMDLib.
        DeferredHolder<Item, MineralBucketItem> bucket = DeferredHolder.create(Registries.ITEM, mineral.id("bucket"));
        DeferredHolder<FluidType, MineralFluidType> type = FLUID_TYPES.register(name, () -> new MineralFluidType(
                FluidType.Properties.create().density(3000).viscosity(6000).temperature(1300)
                        .canSwim(false).canDrown(true).canExtinguish(false).canHydrate(false)
                        .sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL_LAVA)
                        .sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY_LAVA),
                mineral.colour()));
        // As in 1.13.2: molten minerals flow slowly and never form new sources.
        BaseFlowingFluid.Properties properties = new BaseFlowingFluid.Properties(type, source, flowing)
                .bucket(bucket).block(block)
                .slopeFindDistance(2).levelDecreasePerBlock(2).tickRate(30).explosionResistance(100.0F);
        FLUIDS.register(name, () -> new BaseFlowingFluid.Source(properties));
        FLUIDS.register("flowing_" + name, () -> new BaseFlowingFluid.Flowing(properties));
        BLOCKS.register(name, () -> new MoltenMineralBlock(source.get(), BlockBehaviour.Properties.of()
                .mapColor(MapColor.FIRE).replaceable().noCollission().strength(100.0F)
                .pushReaction(PushReaction.DESTROY).noLootTable().liquid()));
        // Older versions registered an item for each molten block. Keep it, outside the
        // creative tab, so stacks in old saves survive.
        ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
        FLUID_CONTENT.put(name, new FluidContent(type, source, flowing, block, bucket));
    }

    public static RuntimeContentRegistrar registrar() {
        return registrar;
    }

    /** The molten fluids, by mineral name. */
    public static Map<String, FluidContent> fluids() {
        return Collections.unmodifiableMap(FLUID_CONTENT);
    }
}
