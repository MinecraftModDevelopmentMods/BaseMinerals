package zone.moddev.mc.baseminerals.content;

import java.util.LinkedHashMap;
import java.util.Map;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFlowingFluid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.fluid.FlowingFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.init.Items;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.IRegistry;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import zone.moddev.mc.baseminerals.BaseMinerals;

/** Registry names are kept from the 1.10–1.12 builds. */
@Mod.EventBusSubscriber(modid = BaseMinerals.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class MineralContent {
    private static final String[] MATERIALS = {
        "lithium", "niter", "phosphorus", "potash", "salt", "saltpeter", "silicon", "sulfur"
    };
    private static final Map<String, Block> BLOCKS = new LinkedHashMap<>();
    private static final Map<String, Molten> MOLTEN = new LinkedHashMap<>();
    private static final ItemGroup GROUP = new ItemGroup(BaseMinerals.MOD_ID) {
        @Override public ItemStack createIcon() { return new ItemStack(BLOCKS.get("sulfur_ore")); }
    };

    private MineralContent() {}

    // Vanilla's fluid registries are shared, so use Forge's sequential block event.
    private static void initializeFluids() {
        for (String name : new String[] {"lithium", "silicon"}) {
            Molten molten = new Molten();
            MOLTEN.put(name, molten);
            molten.flowing = new MoltenMineralFluid.Flowing(molten);
            molten.source = new MoltenMineralFluid.Source(molten);
            IRegistry.field_212619_h.put(id("flowing_" + name), molten.flowing);
            IRegistry.field_212619_h.put(id(name), molten.source);
            for (Fluid fluid : new Fluid[] {molten.flowing, molten.source}) {
                fluid.getStateContainer().getValidStates().forEach(Fluid.STATE_REGISTRY::add);
            }
        }
    }

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        initializeFluids();
        for (String name : MATERIALS) {
            putBlock(event, name + "_block", new MineralBlock(false));
            if (!"silicon".equals(name)) putBlock(event, name + "_ore", new MineralBlock(true));
        }
        MOLTEN.forEach((name, molten) -> {
            molten.block = new BlockFlowingFluid(molten.source, Block.Properties.create(Material.LAVA)
                    .doesNotBlockMovement().hardnessAndResistance(100.0F).variableOpacity()) {};
            putBlock(event, name, molten.block);
        });
    }

    private static void putBlock(RegistryEvent.Register<Block> event, String name, Block block) {
        block.setRegistryName(id(name));
        BLOCKS.put(name, block);
        event.getRegistry().register(block);
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        BLOCKS.forEach((name, block) -> putItem(event, name, new ItemBlock(block,
                MOLTEN.containsKey(name) ? new Item.Properties() : itemProperties())));
        for (String name : MATERIALS) {
            putItem(event, name + "_powder", new Item(itemProperties()));
            putItem(event, name + "_smallpowder", new Item(itemProperties()));
        }
        MOLTEN.forEach((name, molten) -> {
            putItem(event, name + "_ingot", new Item(itemProperties()));
            putItem(event, name + "_nugget", new Item(itemProperties()));
            molten.bucket = new ItemBucket(molten.source, itemProperties().containerItem(Items.BUCKET).maxStackSize(1));
            putItem(event, name + "_bucket", molten.bucket);
        });
        putItem(event, "silicon_blend", new Item(itemProperties()));
        putItem(event, "silicon_smallblend", new Item(itemProperties()));
    }

    private static void putItem(RegistryEvent.Register<Item> event, String name, Item item) {
        event.getRegistry().register(item.setRegistryName(id(name)));
    }

    private static Item.Properties itemProperties() { return new Item.Properties().group(GROUP); }
    public static ResourceLocation id(String name) { return new ResourceLocation(BaseMinerals.MOD_ID, name); }
    public static boolean ownsFluid(Fluid fluid) { return fluid instanceof MoltenMineralFluid; }

    static final class Molten {
        FlowingFluid source;
        FlowingFluid flowing;
        BlockFlowingFluid block;
        Item bucket;
    }

    private static final class MineralBlock extends Block {
        private final boolean ore;

        MineralBlock(boolean ore) {
            super(Block.Properties.create(Material.ROCK).hardnessAndResistance(3.0F, 5.0F));
            this.ore = ore;
        }

        @Override public ToolType getHarvestTool(IBlockState state) { return ToolType.PICKAXE; }
        @Override public int getHarvestLevel(IBlockState state) { return 1; }
        @Override @OnlyIn(Dist.CLIENT)
        public BlockRenderLayer getRenderLayer() {
            return ore ? BlockRenderLayer.CUTOUT_MIPPED : BlockRenderLayer.SOLID;
        }
    }
}
