package celestibytes.celtweaker.modules;

import java.util.Iterator;
import java.util.List;

import mods.defeatedcrow.api.recipe.ITeaRecipe;
import mods.defeatedcrow.api.recipe.RecipeRegisterManager;
import net.minecraft.item.ItemStack;
import celestibytes.celtweaker.api.AModule;
import celestibytes.celtweaker.api.ModuleUtil;
import celestibytes.celtweaker.api.Tweak;
import celestibytes.celtweaker.api.Version;

public class AMT2_DelTeaRecipe extends AModule {

	public AMT2_DelTeaRecipe() {
		super("amt2_del_tearecipe", new Version(0, 1), typeList(ItemStack.class));
	}

	@Override
	public boolean apply(Tweak tweak) {
		ItemStack target = (ItemStack) tweak.args[0];
		
		boolean del = false;
		
		List<? extends ITeaRecipe> recipes = RecipeRegisterManager.teaRecipe.getRecipeList();
		Iterator<? extends ITeaRecipe> iter = recipes.iterator();
		while(iter.hasNext()) {
			ITeaRecipe rec = iter.next();
			if(ModuleUtil.matches(rec.getOutput(), target)) {
				iter.remove();
				del = true;
			}
		}
		
		return del;
	}

	@Override
	public String[] getSamples() {
		return new String[]{
				"# Format: amt2_deltearecipe;result",
				"# Example: amt2_deltearecipe;[some itemstack]"
		};
	}

}
