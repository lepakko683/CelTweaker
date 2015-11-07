package celestibytes.celtweaker.modules;

import java.util.Iterator;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import celestibytes.celtweaker.api.AModule;
import celestibytes.celtweaker.api.ModuleUtil;
import celestibytes.celtweaker.api.Tweak;
import celestibytes.celtweaker.api.Version;

public class DelVanillaCrafting extends AModule{

	public DelVanillaCrafting() {
		super("del_vanillacrafting", new Version(0, 1), typeList(ItemStack.class));
	}

	@Override
	public boolean apply(Tweak tweak) {
		ItemStack target = (ItemStack) tweak.args[0];
		
		boolean del = false;
		
		Iterator<?> recipes = CraftingManager.getInstance().getRecipeList().iterator();
		while(recipes.hasNext()) {
			Object rec = recipes.next();
			if(rec == null) {
				continue;
			}
			
			ItemStack output = null;
			
			if(rec instanceof ShapedRecipes) {
				ShapedRecipes re = (ShapedRecipes) rec;
				output = re.getRecipeOutput();
			} else if(rec instanceof ShapelessRecipes) {
				ShapelessRecipes re = (ShapelessRecipes) rec;
				output = re.getRecipeOutput();
			} else if(rec instanceof ShapedOreRecipe) {
				ShapedOreRecipe re = (ShapedOreRecipe) rec;
				output = re.getRecipeOutput();
			} else if(rec instanceof ShapelessOreRecipe) {
				ShapelessOreRecipe re = (ShapelessOreRecipe) rec;
				output = re.getRecipeOutput();
			}
			
			if(output != null && ModuleUtil.matches(output, target)) {
				recipes.remove();
				del = true;
			}
		}
		
		return del;
	}
	
	@Override
	public String[] getSamples() {
		return new String[]{
				"# Format: delvanillacrafting;target",
				"#     target: ItemStack which's recipes to remove",
				"# Example: delvanillacrafting;minecraft:beacon"
		};
	}
	
}
