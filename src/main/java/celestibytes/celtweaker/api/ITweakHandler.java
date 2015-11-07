package celestibytes.celtweaker.api;

import java.util.List;

import celestibytes.celtweaker.api.types.TBase;

public interface ITweakHandler {
	
	/** @param args first element must of type TString, and be a name of a tweak module. */
	public Tweak handleTweak(List<TBase> args, ILogger log);
	
}
