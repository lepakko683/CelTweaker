package celestibytes.celtweaker.mods;

import java.util.Arrays;
import java.util.List;

import cpw.mods.fml.common.Loader;
import celestibytes.celtweaker.api.AModule;
import celestibytes.celtweaker.api.IModuleProvider;
import celestibytes.celtweaker.modules.AMT2_DelIceRecipe;
import celestibytes.celtweaker.modules.AMT2_DelTeaRecipe;

public class AMT2 implements IModuleProvider {
	
	private final String name = "DCsAppleMilk";

	@Override
	public List<AModule> getModules() {
		return Arrays.asList(new AMT2_DelIceRecipe(), new AMT2_DelTeaRecipe());
	}

	@Override
	public boolean shouldBeLoaded() {
		return Loader.isModLoaded(name);
	}

	@Override
	public String getProviderName() {
		return name;
	}

}
