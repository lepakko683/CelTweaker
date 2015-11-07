package celestibytes.celtweaker.json;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagList;

public class JSInteger extends JSObject {
	
	public int value;
	
	public JSInteger(String key, int value) {
		super(JSObject.NBT_INT, key);
		this.value = value;
	}

	@Override
	public void writeValue(NBTTagCompound nbt) {
		nbt.setInteger(this.key, value);
	}

	@Override
	public void writeValue(NBTTagList nbt) {
		nbt.appendTag(new NBTTagInt(value));
	}
}
