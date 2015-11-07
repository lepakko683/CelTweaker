package celestibytes.celtweaker.mods;

import java.util.Arrays;
import java.util.List;

import cpw.mods.fml.common.Loader;
import celestibytes.celtweaker.api.AModule;
import celestibytes.celtweaker.api.IModuleProvider;
import celestibytes.celtweaker.modules.NewDawnBotania_DelRunicAltar;
import celestibytes.celtweaker.modules.NewDawnBotania_ManaInfusion;
import celestibytes.celtweaker.modules.NewDawnBotania_RunicAltar;

public class Botania implements IModuleProvider {
	
	// TODO: rename all NewDawnBotania things to just Botania.
	
	private final String name = "Botania";

	@Override
	public List<AModule> getModules() {
		return Arrays.asList(new NewDawnBotania_RunicAltar(), new NewDawnBotania_DelRunicAltar(), new NewDawnBotania_ManaInfusion());
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
