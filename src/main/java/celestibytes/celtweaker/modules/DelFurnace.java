package celestibytes.celtweaker.modules;

import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraftforge.oredict.OreDictionary;
import celestibytes.celtweaker.api.AModule;
import celestibytes.celtweaker.api.Tweak;
import celestibytes.celtweaker.api.Version;

public class DelFurnace extends AModule {

	public DelFurnace() {
		super("del_furnace", new Version(0, 1), typeList(ItemStack.class));
	}

	@Override
	public boolean apply(Tweak tweak) {
		ItemStack target = (ItemStack) tweak.args[0];
		Set<?> recipes = FurnaceRecipes.smelting().getSmeltingList().entrySet();
		Iterator<?> iter = recipes.iterator();
		
		boolean ret = false;
		
		while(iter.hasNext()) {
			Entry<?,?> o = (Entry<?,?>) iter.next();
			ItemStack result = (ItemStack) o.getValue();
			
			if(matchStacks(target, result)) {
				iter.remove();
				ret = true;
			}
			
		}
		
		return ret;
	}
	
	private boolean matchStacks(ItemStack target, ItemStack result) {
		return target != null && result != null && target.getItem() == result.getItem() && (target.getItemDamage() == OreDictionary.WILDCARD_VALUE || (target.getItemDamage() == result.getItemDamage()));
	}

	@Override
	public String[] getSamples() {
		return null;
	}

}
