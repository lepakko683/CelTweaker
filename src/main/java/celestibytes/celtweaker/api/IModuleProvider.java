package celestibytes.celtweaker.api;

import java.util.List;

public interface IModuleProvider {
	
	public List<AModule> getModules();
	
	/** Usually modid */
	public String getProviderName();
	
	public boolean shouldBeLoaded();
	
}
