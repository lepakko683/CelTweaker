package celestibytes.celtweaker.api.types;

public class TNumber extends TBase {
	
	public final double value;

	public TNumber(String raw) {
		super(raw);
		
		value = Double.parseDouble(raw);
	}
	
	public TNumber(int value) {
		super(Integer.toString(value));
		
		this.value = value;
	}
	
	@Override
	public String toString() {
		return "{number=" + value + ",raw=\"" + raw + "\"}";
	}
	
}
