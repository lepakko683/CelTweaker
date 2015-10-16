package celestibytes.celtweaker.mods;

import java.util.Arrays;
import java.util.List;

import celestibytes.celtweaker.api.AModule;
import celestibytes.celtweaker.api.IModuleProvider;
import celestibytes.celtweaker.modules.ChestLoot;
import celestibytes.celtweaker.modules.DelVanillaCrafting;
import celestibytes.celtweaker.modules.MobLoot;
import celestibytes.celtweaker.modules.VanillaCrafting;

// Minecraft, best mod!
public class Minecraft implements IModuleProvider {

	@Override
	public List<AModule> getModules() {
		return Arrays.asList(new VanillaCrafting(), new DelVanillaCrafting(), new MobLoot(), new ChestLoot());
	}

	@Override
	public boolean shouldBeLoaded() {
		return true;
	}

	@Override
	public String getProviderName() {
		return "Minecraft";
	}
	
}
