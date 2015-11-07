package celestibytes.celtweaker.api.types;

public class TString extends TBase {
	
	public TString(String raw) {
		super(raw);
	}
	
	@Override
	public String toString() {
		return "{string,raw=\"" + raw + "\"}";
	}

}
