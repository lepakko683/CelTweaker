package celestibytes.celtweaker.modules;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapedOreRecipe;
import celestibytes.celtweaker.api.AModule;
import celestibytes.celtweaker.api.Tweak;
import celestibytes.celtweaker.api.Version;

public class VanillaCrafting extends AModule {

	public VanillaCrafting() {
		super("vanillacrafting", new Version(0, 1), typeList(new ArgRepeat(1, 3, typeList(String.class)), new ArgRepeat(typeList(Character.class, new ArgOr(ItemStack.class, String.class))), ItemStack.class));
	}
	
	@Override
	public boolean apply(Tweak tweak) {
		Object[] recipe = new Object[tweak.args.length - 1];
		System.arraycopy(tweak.args, 0, recipe, 0, recipe.length);
		
		System.out.println("Adding recipe...");
		GameRegistry.addRecipe(new ShapedOreRecipe((ItemStack) tweak.args[tweak.args.length - 1], recipe));
		
		return true;
	}

	@Override
	public String[] getSamples() {
		return new String[] { // TODO: samples in old syntax, update to the new syntax!
				"# Format: vanillacrafting;shape;shape;shape;key;value;key;value;result",
				"#     Key is a single ASCII character. And value is either an itemstack or an ore dictionary name.",
				"# Example: vanillacrafting;\"DD\";\"II\";'D';minecraft:dirt;'I';minecraft:iron_ingot;minecraft:diamond,2"
		};
	}

}
