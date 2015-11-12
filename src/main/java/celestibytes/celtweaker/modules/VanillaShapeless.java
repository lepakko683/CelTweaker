package celestibytes.celtweaker.modules;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import celestibytes.celtweaker.api.AModule;
import celestibytes.celtweaker.api.Tweak;
import celestibytes.celtweaker.api.Version;

public class VanillaShapeless extends AModule {

	public VanillaShapeless() {
		super("vanillashapeless", new Version(0, 1), typeList(ItemStack.class, new ArgRepeat(1, 9, typeList(new ArgOr(ItemStack.class, String.class)))));
	}

	@Override
	public boolean apply(Tweak tweak) {
		ItemStack result = (ItemStack) tweak.args[0];
		Object[] recipe = new Object[tweak.args.length - 1];
		System.arraycopy(tweak.args, 1, recipe, 0, recipe.length);
		
		GameRegistry.addRecipe(new ShapelessOreRecipe(result, recipe));
		
		return true;
	}

	@Override
	public String[] getSamples() {
		return null;
	}

}
