package celestibytes.celtweaker.modules;

import java.util.Iterator;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import celestibytes.celtweaker.AHandlerModule;

public class DelVanillaCrafting extends AHandlerModule{

	public DelVanillaCrafting() {
		super("delvanillacrafting", typeList(ItemStack.class));
	}

	@Override
	public void handle(Object[] args) {
		ItemStack target = (ItemStack) args[0];
		
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
			
			if(output != null && matches(output, target)) {
				recipes.remove();
			}
		}
	}
	
	private boolean matches(ItemStack a, ItemStack b) {
		return a == null || b == null ? false : a.getItem() == b.getItem() ? a.getItemDamage() == b.getItemDamage() : false;
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
