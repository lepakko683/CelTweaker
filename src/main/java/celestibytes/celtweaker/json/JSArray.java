package celestibytes.celtweaker.json;

import java.util.LinkedList;
import java.util.List;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class JSArray extends JSObject {
	
	private List<JSObject> entries = new LinkedList<JSObject>();
	private int typeLock = -1;

	public JSArray(String key, JSObject... ents) {
		super(JSObject.NBT_ARRAY, key);
		
		for(JSObject e : ents) {
			if(typeLock == -1) {
				typeLock = e.type;
			} else {
				if(typeLock != e.type) {
					throw new IllegalArgumentException("All types in a JSArray must be of the same type!");
				}
			}
			
			entries.add(e);
		}
	}

	@Override
	public void writeValue(NBTTagCompound nbt) {
		NBTTagList ths = new NBTTagList();
		for(JSObject obj : entries) {
			obj.writeValue(ths);
		}
		
		nbt.setTag(this.key, ths);
	}

	@Override
	public void writeValue(NBTTagList nbt) {
		NBTTagList ths = new NBTTagList();
		for(JSObject obj : entries) {
			obj.writeValue(ths);
		}
		
		nbt.appendTag(ths);
	}

}
