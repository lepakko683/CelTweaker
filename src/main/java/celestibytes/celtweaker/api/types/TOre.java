package celestibytes.celtweaker.api.types;

import net.minecraftforge.oredict.OreDictionary;

public class TOre extends TBase {

	public TOre(String raw) {
		super(raw);
		
		if(!OreDictionary.doesOreNameExist(raw)) {
			throw new IllegalArgumentException("Unknown ore: " + raw);
		}
	}
	
	@Override
	public String toString() {
		return "{ore,raw=\"" + raw + "\"}";
	}

}
