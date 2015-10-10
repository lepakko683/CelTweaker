package celestibytes.celtweaker.modules;

import net.minecraft.item.ItemStack;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraftforge.common.ChestGenHooks;
import celestibytes.celtweaker.AHandlerModule;

public class ChestLoot extends AHandlerModule {

	public ChestLoot() {// min, max, weight: 1-10
		super("chestloot", typeList(String.class, ItemStack.class, Integer.class, Integer.class, Integer.class));
	}

	@Override
	/* String:where ItemStack:what Integer:min Integer:max Integer:weight */
	public void handle(Object[] args) {
		System.out.println("addLoot!");
		ChestGenHooks.addItem((String)args[0], new WeightedRandomChestContent((ItemStack)args[1], (Integer)args[2], (Integer)args[3], (Integer)args[4]));
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
