package celestibytes.celtweaker.modules;

import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.registry.GameRegistry;
import celestibytes.celtweaker.api.AModule;
import celestibytes.celtweaker.api.Tweak;
import celestibytes.celtweaker.api.Version;

public class Furnace extends AModule {

	public Furnace() {
		super("furnace", new Version(0, 1), typeList(ItemStack.class, ItemStack.class, Float.class));
	}

	@Override
	public boolean apply(Tweak tweak) {
		ItemStack input = (ItemStack) tweak.args[0];
		ItemStack output = (ItemStack) tweak.args[1];
		float xp = (Float) tweak.args[2];
		
		GameRegistry.addSmelting(input, output, xp);
		
		return true;
	}

	@Override
	public String[] getSamples() {
		return null;
	}
}
