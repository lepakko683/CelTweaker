package celestibytes.celtweaker.api.types;

public class TChar extends TBase {

	public final char value;
	
	public TChar(String raw) {
		super(raw);
		value = raw.charAt(0);
	}
	
	public TChar(char c) {
		super(Character.toString(c));
		value = c;
	}
	
	@Override
	public String toString() {
		return "{char='" + value + "',raw=\"" + raw + "\"}";
	}

}
