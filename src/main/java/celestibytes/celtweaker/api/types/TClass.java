package celestibytes.celtweaker.api.types;

import celestibytes.celtweaker.api.CelTweakerException;

public class TClass extends TBase {
	
	public final Class<?> clazz;

	public TClass(String raw) {
		super(raw);
		
		try {
			Class<?> cls = Class.forName(raw);
			clazz = cls;
		} catch(Exception e) {
			e.printStackTrace();
			
			throw new CelTweakerException("Unable to create instace of TClass: " + raw);
		}
	}

}
