package mmd.baseminerals.init;

/**
 *
 * @author Jasmine Iwanek
 *
 */
public class Recipes extends cyano.basemetals.init.Recipes {

	private static boolean initDone = false;

	/**
	 *
	 */
	public static void init() {
		if (initDone) {
			return;
		}

		Materials.init();
		Blocks.init();
		Items.init();

		initModSpecificRecipes();

		initDone = true;
	}

	private static void initModSpecificRecipes() {
		// alloy blends
//		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(Items.charcoal_blend, 2), "dustCharcoal", "dustCharcoal"));

		// small alloy blends
//		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(Items.charcoal_smallblend, 2), "smalldustCharcoal", "smalldustCharcoal"));
	}
}
