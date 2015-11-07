package celestibytes.celtweaker.modules;

import java.util.Iterator;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.recipe.RecipeRuneAltar;
import net.minecraft.item.ItemStack;
import celestibytes.celtweaker.api.AModule;
import celestibytes.celtweaker.api.ModuleUtil;
import celestibytes.celtweaker.api.Tweak;
import celestibytes.celtweaker.api.Version;
import celestibytes.celtweaker.api.types.TBase;
import celestibytes.celtweaker.api.types.TOre;

public class NewDawnBotania_DelRunicAltar extends AModule {

	public NewDawnBotania_DelRunicAltar() {
		super("newdawnbotania_del_runicaltar", new Version(0, 1), null);
	}
	
	@Override
	public Tweak checkArgs(final TBase[] args, final String cfgName, final int lineNumber) {
		Object[] ret = new Object[args.length];
		
		ret[0] = args[0].castStack().value;
		if(ret[0] == null) {
			return null;
		}
		
//		if(isBoolean(args[1])) {
//			ret[1] = args[1].equalsIgnoreCase("true");
//		} else {
//			return null;
//		}
		
//		for(int i=2;i<args.length;i++) {
//			if(args[i] instanceof TOre) {
//				ret[i] = args[i].castOre().asString();
//			} else {
//				ret[i] = args[i].castStack().value;
//				if(ret[i] == null) {
//					return null;
//				}
//			}
//		}
		
		return new Tweak(this, cfgName, lineNumber, ret);
	}

	@Override
	public boolean apply(Tweak tweak) { // TODO: add filtering with inputs
		ItemStack target = (ItemStack) tweak.args[0];
		//boolean exact = (Boolean) args[1];
		
		boolean del = false;
		
		Iterator<RecipeRuneAltar> iter = BotaniaAPI.runeAltarRecipes.iterator();
		while(iter.hasNext()) {
			RecipeRuneAltar rec = iter.next();
			if(ItemStack.areItemStacksEqual(rec.getOutput(), target)) {
				iter.remove();
				del = true;
			}
		}
		
		return del;
	}
	
	@Override
	public String[] getSamples() {
		return new String[]{
				"# Format: newdawnbotania_delrunicaltar;target",
				"# target: itemstack which's recipes to remove",
				"# Example: newdawnbotania_delrunicaltar;Botania:[Can't remember an itemid while writing this...]"
		};
	}

}
