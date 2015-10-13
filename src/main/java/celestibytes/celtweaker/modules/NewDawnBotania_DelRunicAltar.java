package celestibytes.celtweaker.modules;

import java.util.Iterator;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.recipe.RecipeRuneAltar;
import net.minecraft.item.ItemStack;
import celestibytes.celtweaker.AHandlerModule;
import celestibytes.celtweaker.Util;

public class NewDawnBotania_DelRunicAltar extends AHandlerModule {

	public NewDawnBotania_DelRunicAltar() {
		super("newdawnbotania_delrunicaltar", null);
	}
	
	@Override
	public Object[] checkArgs(String[] args) {
		Object[] ret = new Object[args.length];
		
		ret[0] = parseItemStack(args[0]);
		if(ret[0] == null) {
			return null;
		}
		
//		if(isBoolean(args[1])) {
//			ret[1] = args[1].equalsIgnoreCase("true");
//		} else {
//			return null;
//		}
		
		for(int i=2;i<args.length;i++) {
			if(isOreString(args[0])) {
				ret[i] = Util.unquote(args[i]);
			} else {
				ret[i] = parseItemStack(args[i]);
				if(ret[i] == null) {
					return null;
				}
			}
		}
		
		return ret;
	}

	@Override
	public void handle(Object[] args) { // TODO: add filtering with inputs
		ItemStack target = (ItemStack) args[0];
		//boolean exact = (Boolean) args[1];
		
		Iterator<RecipeRuneAltar> iter = BotaniaAPI.runeAltarRecipes.iterator();
		while(iter.hasNext()) {
			RecipeRuneAltar rec = iter.next();
			if(ItemStack.areItemStacksEqual(rec.getOutput(), target)) {
				iter.remove();
			}
		}
		
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
