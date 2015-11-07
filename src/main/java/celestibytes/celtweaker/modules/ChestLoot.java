package celestibytes.celtweaker.modules;

import net.minecraft.item.ItemStack;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraftforge.common.ChestGenHooks;
import celestibytes.celtweaker.api.AModule;
import celestibytes.celtweaker.api.Tweak;
import celestibytes.celtweaker.api.Version;

public class ChestLoot extends AModule {

	public ChestLoot() {// min, max, weight: 1-10
		super("chestloot", new Version(0, 1), typeList(String.class, ItemStack.class, Integer.class, Integer.class, Integer.class));
	}

	@Override
	/* String:where ItemStack:what Integer:min Integer:max Integer:weight */
	public boolean apply(Tweak tweak) {
		System.out.println("addLoot!");
		ChestGenHooks.addItem((String) tweak.args[0], new WeightedRandomChestContent((ItemStack) tweak.args[1], (Integer) tweak.args[2], (Integer) tweak.args[3], (Integer) tweak.args[4]));
		return true;
	}

	@Override
	public String[] getSamples() {
		return new String[]{
				"# Format: chestloot;target;stack;minItems;maxItems;weight(1-10)",
				"# Target one of: mineshaftCorridor, pyramidDesertyChest, pyramidJungleChest,",
				"#     pyramidJungleDispenser, strongholdCorridor, strongholdLibrary,",
				"#     strongholdCrossing, villageBlacksmith, bonusChest, dungeonChest",
				"# Example: chestloot;\"bonusChest\";minecraft:beacon;1;64;10"
		};
	}

}
