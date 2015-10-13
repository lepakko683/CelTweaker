package celestibytes.celtweaker.modules;

import net.minecraft.item.ItemStack;
import vazkii.botania.api.BotaniaAPI;
import celestibytes.celtweaker.AHandlerModule;
import celestibytes.celtweaker.Util;

public class NewDawnBotania_ManaInfusion extends AHandlerModule {

	public NewDawnBotania_ManaInfusion() {
		super("newdawnbotania_manainfusion", typeList(ItemStack.class, Util.OreStringOrItemStack.class, Integer.class));
	}

	@Override
	public void handle(Object[] args) {
		if(args[1] instanceof ItemStack) {
			BotaniaAPI.registerManaInfusionRecipe((ItemStack) args[0], (ItemStack) args[1], (Integer) args[2]);
		} else if(args[1] instanceof Util.OreString) {
			BotaniaAPI.registerManaInfusionRecipe((ItemStack) args[0], ((Util.OreString) args[1]).ore, (Integer) args[2]);
		}
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
