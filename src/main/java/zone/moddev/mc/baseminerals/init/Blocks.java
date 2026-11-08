package zone.moddev.mc.baseminerals.init;

import java.util.Arrays;
import java.util.List;

import zone.moddev.mc.baseminerals.block.OverlayOreBlock;
import zone.moddev.mc.baseminerals.data.MaterialNames;
import com.mcmoddev.lib.data.Names;
import com.mcmoddev.lib.data.SharedStrings;
import com.mcmoddev.lib.init.Materials;
import com.mcmoddev.lib.material.MMDMaterial;
import com.mcmoddev.lib.util.Oredicts;
import net.minecraft.block.Block;

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
/*
		if (zone.moddev.mc.baseminerals.util.Config.isEnabled(MaterialNames.CHARCOAL) && Materials.hasMaterial(MaterialNames.CHARCOAL)) {
			create(Names.BLOCK, Materials.getMaterialByName(MaterialNames.CHARCOAL));
		}
*/
		final List<String> materials = Arrays.asList(MaterialNames.CHARCOAL, MaterialNames.LITHIUM, MaterialNames.NITER,
				MaterialNames.PHOSPHORUS, MaterialNames.POTASH, MaterialNames.SALT, MaterialNames.SALTPETER,
				MaterialNames.SILICON, MaterialNames.SULFUR);

		materials.stream().filter(zone.moddev.mc.baseminerals.util.Config::isEnabled).filter(Materials::hasMaterial).forEach(materialName -> {
			final MMDMaterial material = Materials.getMaterialByName(materialName);

			create(Names.BLOCK, material);
			createOre(material);
		});
/*
		if (zone.moddev.mc.baseminerals.util.Config.isEnabled(MaterialNames.SILICON) && Materials.hasMaterial(MaterialNames.SILICON)) {
			create(Names.BLOCK, Materials.getMaterialByName(MaterialNames.SILICON));
		}
*/
	}

	private static void createOre(MMDMaterial material) {
		if (!material.hasOre() || material.hasBlock(Names.ORE) || !isNameEnabled(Names.ORE)) {
			return;
		}
		final Block ore = addBlock(new OverlayOreBlock(material), Names.ORE, material,
				ItemGroups.getTab(SharedStrings.TAB_BLOCKS));
		material.addNewBlock(Names.ORE.toString(), ore);
		Oredicts.registerOre("ore" + material.getCapitalizedName(), ore);
	}
}
