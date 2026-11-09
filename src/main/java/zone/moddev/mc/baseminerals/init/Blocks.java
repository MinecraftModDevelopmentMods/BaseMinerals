package zone.moddev.mc.baseminerals.init;

import java.util.Arrays;
import java.util.List;

import zone.moddev.mc.baseminerals.BaseMinerals;
import zone.moddev.mc.baseminerals.data.MaterialNames;
import zone.moddev.mc.baseminerals.block.OverlayOreBlock;
import com.mcmoddev.lib.util.Oredicts;
import com.mcmoddev.lib.data.Names;
import com.mcmoddev.lib.data.SharedStrings;
import com.mcmoddev.lib.material.MMDMaterial;

import net.minecraft.block.Block;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * This class initializes all blocks in Base Minerals.
 *
 * @author Jasmine Iwanek
 *
 */
public class Blocks extends com.mcmoddev.lib.init.Blocks {

	protected Blocks() {
		throw new IllegalAccessError(SharedStrings.NOT_INSTANTIABLE);
	}

	/**
	 *
	 */
	public static void init() {
		final List<String> materials = Arrays.asList(MaterialNames.LITHIUM, MaterialNames.NITER,
				MaterialNames.PHOSPHORUS, MaterialNames.POTASH, MaterialNames.SALT, MaterialNames.SALTPETER,
				MaterialNames.SILICON, MaterialNames.SULFUR);

		materials.stream().filter(Materials::hasMaterial)
				.filter(materialName -> !Materials.getMaterialByName(materialName).isEmpty())
				.forEach(materialName -> {
					final MMDMaterial material = Materials.getMaterialByName(materialName);

					create(Names.BLOCK, material);
					if (material.hasOre() && !material.hasBlock(Names.ORE) && isNameEnabled(Names.ORE)) {
                        final Block ore = addBlock(new OverlayOreBlock(material), Names.ORE, material,
                                ItemGroups.getTab(BaseMinerals.MODID, SharedStrings.TAB_BLOCKS));
                        material.addNewBlock(Names.ORE.toString(), ore);
                        Oredicts.registerOre("ore" + material.getCapitalizedName(), ore);
                    }
				});
	}

	@SubscribeEvent
	public static void registerBlocks(RegistryEvent.Register<Block> event) {
		for (MMDMaterial material : Materials.getMaterialsByMod(BaseMinerals.MODID)) {
			for (Block block : material.getBlocks()) {
				if (block.getRegistryName().getNamespace().equals(BaseMinerals.MODID)) {
					event.getRegistry().register(block);
				}
			}
		}
	}
}
