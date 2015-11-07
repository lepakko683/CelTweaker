package celestibytes.celtweaker.modules;

import net.minecraft.item.ItemStack;
import vazkii.botania.api.BotaniaAPI;
import celestibytes.celtweaker.api.AModule;
import celestibytes.celtweaker.api.ModuleUtil;
import celestibytes.celtweaker.api.Tweak;
import celestibytes.celtweaker.api.Version;
import celestibytes.celtweaker.api.types.TBase;
import celestibytes.celtweaker.api.types.TOre;

public class NewDawnBotania_RunicAltar extends AModule {

	public NewDawnBotania_RunicAltar() {
		super("newdawnbotania_runicaltar", new Version(0, 1), null);
	}
	
	@Override
	public Tweak checkArgs(final TBase[] args, final String cfgName, final int lineNumber) {
		if(args.length <= 2) {
			return null;
		}
		
		Object[] ret = new Object[args.length];
		
		if((ret[0] = args[0].castStack().value) == null) {
			return null;
		}

		ret[1] = args[1].castNumber().asInt();
		if(ret[1] == null) {
			return null;
		}
		
		for(int i = 2; i < args.length; i++) {
			if(args[i] instanceof TOre) {
				ret[i] = args[i].castOre().asString();
			} else if((ret[i] = args[i].castStack().value) != null) {
				
			} else {
				return null;
			}
		}
		
		return new Tweak(this, cfgName, lineNumber, ret);
	}

	@Override
	public boolean apply(Tweak tweak) {
		Object[] inputs = new Object[tweak.args.length - 2];
		System.arraycopy(tweak.args, 2, inputs, 0, inputs.length);
		BotaniaAPI.registerRuneAltarRecipe((ItemStack) tweak.args[0], (Integer) tweak.args[1], inputs);
		
		return true;
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
