package celestibytes.celtweaker.api.types;

public class TBoolean extends TBase {

	public final boolean value;
	
	public TBoolean(String raw) {
		super(raw);
		
		if(raw.equalsIgnoreCase("true")) {
			value = true;
		} else if(raw.equalsIgnoreCase("false")) {
			value = false;
		} else {
			value = false;
			throw new IllegalAccessError("Not valid boolean string: " + raw);
		}
	}
	
	public TBoolean(boolean value) {
		super(value ? "true" : "false");
		
		this.value = value;
	}
	
	@Override
	public String toString() {
		return "{boolean=" + (value ? "true" : "false") + ",raw=\"" + raw + "\"}";
	}

}
