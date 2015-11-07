package celestibytes.celtweaker.json;

import net.minecraft.nbt.NBTTagByte;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class JSByte extends JSObject {

	private byte value;
	
	public JSByte(String key, byte value) {
		super(JSObject.NBT_BYTE, key);
		this.value = value;
	}

	@Override
	public void writeValue(NBTTagCompound nbt) {
		nbt.setByte(this.key, value);
	}

	@Override
	public void writeValue(NBTTagList nbt) {
		nbt.appendTag(new NBTTagByte(value));
	}

}
