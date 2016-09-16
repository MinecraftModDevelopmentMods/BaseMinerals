package baseminerals.init;

import baseminerals.BaseMinerals;
import cyano.basemetals.blocks.BlockMoltenFluid;
import cyano.basemetals.fluids.CustomFluid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fluids.BlockFluidBase;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.HashMap;
import java.util.Map;

/**
 * This class initializes all fluids in Base Minerals and provides some utility
 * methods for looking up fluids.
 *
 * @author DrCyano
 *
 */
public abstract class Fluids {

	static {
		FluidRegistry.enableUniversalBucket();
	}

	public static Fluid fluidLithium = null;
	public static BlockFluidBase fluidBlockLithium = null;

	public static Fluid fluidSilicon = null;
	public static BlockFluidBase fluidBlockSilicon = null;

	private static final Map<Fluid, BlockFluidBase> fluidBlocks = new HashMap<>();
	private static final Map<BlockFluidBase, String> fluidBlockNames = new HashMap<>();

	private static boolean initDone = false;

	/**
	 *
	 */
	public static void init() {
		if(initDone)
			return;

		// fluids
		fluidLithium = newFluid(BaseMinerals.MODID, "lithium", 2000, 10000, 330, 10, 0xFFFFFFFF);

		fluidSilicon = newFluid(BaseMinerals.MODID, "silicon", 2000, 10000, 330, 10, 0xFFFFFFFF);

		// fluid blocks
		fluidBlockLithium = registerFluidBlock(fluidLithium, new BlockMoltenFluid(fluidLithium),"lithium");

		fluidBlockSilicon = registerFluidBlock(fluidSilicon, new BlockMoltenFluid(fluidSilicon),"silicon");

		initDone = true;
	}

	/**
	 *
	 * @param modID
	 */
	@SideOnly(Side.CLIENT)
	public static void bakeModels(String modID) {
		for(final Fluid fluid : fluidBlocks.keySet()) {
			final BlockFluidBase block = fluidBlocks.get(fluid);
			final Item item = Item.getItemFromBlock(block);
			final ModelResourceLocation fluidModelLocation = new ModelResourceLocation(
					modID.toLowerCase() + ":" + fluidBlockNames.get(block), "fluid");
			ModelBakery.registerItemVariants(item);
			ModelLoader.setCustomMeshDefinition(item, stack -> fluidModelLocation);
			ModelLoader.setCustomStateMapper(block, new StateMapperBase() {
				@Override
				protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
					return fluidModelLocation;
				}
			});
		}
	}

	private static Fluid newFluid(String modID, String name, int density, int viscosity, int temperature, int luminosity, int tintColor) {
		Fluid fluid = new CustomFluid(name, new ResourceLocation(modID + ":blocks/molten_metal_still"), new ResourceLocation(modID + ":blocks/molten_metal_flow"), tintColor);
		fluid.setDensity(density);
		fluid.setViscosity(viscosity);
		fluid.setTemperature(temperature);
		fluid.setLuminosity(luminosity);
		fluid.setUnlocalizedName(modID + "." + name);
		FluidRegistry.registerFluid(fluid);
		FluidRegistry.addBucketForFluid(fluid);
		return fluid;
	}

	private static BlockFluidClassic registerFluidBlock(Fluid fluid, BlockFluidClassic block, String name) {
		ResourceLocation location = new ResourceLocation(BaseMinerals.MODID, name);
		block.setRegistryName(location);
		block.setUnlocalizedName(location.toString());
		GameRegistry.register(block);
		block.setCreativeTab(CreativeTabs.MISC);

		final ItemBlock itemBlock = new ItemBlock(block);
		itemBlock.setRegistryName(location);
		itemBlock.setUnlocalizedName(location.toString());
		GameRegistry.register(itemBlock);

		fluidBlocks.put(fluid, block);
		fluidBlockNames.put(block, name);
		return block;
	}
}
