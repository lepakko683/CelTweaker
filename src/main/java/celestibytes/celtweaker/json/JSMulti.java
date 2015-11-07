package celestibytes.celtweaker.json;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class JSMulti extends JSObject {
	
	private Map<String, JSObject> entries = new HashMap<String, JSObject>();

	public JSMulti(int type, String key, JSObject... ents) {
		super(type, key);
		
		for(JSObject obj : ents) {
			entries.put(obj.key, obj);
		}
	}
	

	@Override
	public void writeValue(NBTTagCompound nbt) {
		NBTTagCompound ths = new NBTTagCompound();
		
		for(Entry<String, JSObject> ent : entries.entrySet()) {
			ent.getValue().writeValue(ths);
		}
		
		nbt.setTag(this.key, ths);
	}


	@Override
	public void writeValue(NBTTagList nbt) {
		NBTTagCompound ths = new NBTTagCompound();
		
		for(Entry<String, JSObject> ent : entries.entrySet()) {
			ent.getValue().writeValue(ths);
		}
		
		nbt.appendTag(ths);
	}

}
