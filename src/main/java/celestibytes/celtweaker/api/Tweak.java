package celestibytes.celtweaker.api;

import celestibytes.celtweaker.api.types.TBase;

public final class Tweak {
	public final AModule module;
	public final String cfgName;
	public final int lineNumber;
	
	public final TBase[] args;
	boolean enabled = false;
	
	/** Place for custom data by the module. e.g. to make it easier to undo. */
	public Object customData;
	
	public Tweak(AModule module, String cfgName, int lineNumber, TBase[] args) {
		this.module = module;
		this.cfgName = cfgName;
		this.lineNumber = lineNumber;
		this.args = args;
	}
	
	public String getModuleName() {
		return module.name;
	}
	
	public boolean isUndoable() {
		return module.isUndoable();
	}
	
	public boolean isEnabled() {
		return enabled;
	}
	
	@Override
	public String toString() {
		return module.name + "@" + cfgName + ":" + lineNumber;
	}
	
}
