package celestibytes.celtweaker.api;

public final class Tweak {
	public final String moduleName;
	public final String cfgName;
	public final int lineNumber;
	
	public final Object[] args;
	public final boolean undoable;
	boolean enabled = false;
	
	/** Place for custom data by the module. e.g. to make it easier to undo. */
	public Object customData;
	
	public Tweak(String moduleName, String cfgName, int lineNumber, boolean undoable, Object... args) {
		this.moduleName = moduleName;
		this.cfgName = cfgName;
		this.lineNumber = lineNumber;
		this.undoable = undoable;
		this.args = args;
	}
	
	@Override
	public String toString() {
		return moduleName + "@" + cfgName + ":" + lineNumber + "[undoable: " + undoable + ", enabled: " + enabled + "]";
	}
	
}
