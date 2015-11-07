package celestibytes.celtweaker.modules;

import net.minecraft.item.ItemStack;
import vazkii.botania.api.BotaniaAPI;
import celestibytes.celtweaker.api.AModule;
import celestibytes.celtweaker.api.ModuleUtil;
import celestibytes.celtweaker.api.Tweak;
import celestibytes.celtweaker.api.Version;

public class NewDawnBotania_ManaInfusion extends AModule {

	public NewDawnBotania_ManaInfusion() {
		super("newdawnbotania_manainfusion", new Version(0, 1), typeList(ItemStack.class, ModuleUtil.OreStringOrItemStack.class, Integer.class));
	}

	@Override
	public boolean apply(Tweak tweak) {
		BotaniaAPI.registerManaInfusionRecipe((ItemStack) tweak.args[0], tweak.args[1], (Integer) tweak.args[2]);
//		if(tweak.args[1] instanceof ItemStack) {
//			BotaniaAPI.registerManaInfusionRecipe((ItemStack) args[0], (ItemStack) args[1], (Integer) args[2]);
//		} else if(args[1] instanceof ModuleUtil.OreString) {
//			BotaniaAPI.registerManaInfusionRecipe((ItemStack) args[0], ((ModuleUtil.OreString) args[1]).ore, (Integer) args[2]);
//		}
		
		return true;
	}

	@Override
	public String[] getSamples() {
		return new String[]{
				"# Format: newdawnbotania_manainfusion;result;input;mana",
				"# input: ItemStack or oredict name ",
				"# mana cannot be over 100 000",
				"# Example: newdawnbotania_manainfusion;minecraft:diamond;minecraft:coal;1000"
		};
	}

}
