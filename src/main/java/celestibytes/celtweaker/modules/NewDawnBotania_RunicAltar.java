package celestibytes.celtweaker.modules;

import net.minecraft.item.ItemStack;
import vazkii.botania.api.BotaniaAPI;
import celestibytes.celtweaker.AHandlerModule;
import celestibytes.celtweaker.Util;

public class NewDawnBotania_RunicAltar extends AHandlerModule {

	public NewDawnBotania_RunicAltar() {
		super("newdawnbotania_runicaltar", null);
	}
	
	@Override
	public Object[] checkArgs(String[] args) {
		if(args.length <= 2) {
			return null;
		}
		
		Object[] ret = new Object[args.length];
		
		if((ret[0] = parseItemStack(args[0])) == null) {
			return null;
		}
		
		if(isInt(args[1])) {
			ret[1] = Integer.parseInt(args[1]);
		} else {
			return null;
		}
		
		for(int i = 2; i < args.length; i++) {
			if(isOreString(args[i])) {
				ret[i] = Util.unquote(args[i]);
			} else if((ret[i] = parseItemStack(args[i])) != null) {
				
			} else {
				return null;
			}
		}
		
		return ret;
	}

	@Override
	public void handle(Object[] args) {
		Object[] inputs = new Object[args.length - 2];
		System.arraycopy(args, 2, inputs, 0, inputs.length);
		BotaniaAPI.registerRuneAltarRecipe((ItemStack) args[0], (Integer) args[1], inputs);
	}

	@Override
	public String[] getSamples() {
		return new String[]{
				"# Format: newdawnbotania_runicaltar;result;mana;input1;input2 etc",
				"# mana: must be 100000 or less",
				"# inputs either single item ItemStack's or oredict names",
				"# Example: newdawnbotania_runicaltar;minecraft:gold_ingot;5000;<ingotIron>;<ingotIron>"
		};
	}

}
