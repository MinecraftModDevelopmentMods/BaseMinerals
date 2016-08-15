package baseminerals.items;

import baseminerals.init.Achievements;
import baseminerals.init.Materials;
import cyano.basemetals.material.MetalMaterial;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/**
 * Ingots
 * @author Jasmine Iwanek
 *
 */
public class ItemMetalIngot extends cyano.basemetals.items.ItemMetalIngot {

	/**
	 * 
	 * @param metal
	 */
	public ItemMetalIngot(MetalMaterial metal) {
		super(metal);
	}

	@Override
	public void onCreated(final ItemStack item, final World world, final EntityPlayer entityPlayer) {
		super.onCreated(item, world, entityPlayer);
		entityPlayer.addStat(cyano.basemetals.init.Achievements.this_is_new, 1);

		if(metal == Materials.charcoal)
			entityPlayer.addStat(Achievements.charcoal_maker, 1);
	}
}
