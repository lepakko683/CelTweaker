package celestibytes.celtweaker.json;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagShort;

public class JSShort extends JSObject {
	
	private short value;

	public JSShort(String key, short value) {
		super(JSObject.NBT_SHORT, key);
		this.value = value;
	}

	@Override
	public void writeValue(NBTTagCompound nbt) {
		nbt.setShort(this.key, value);
	}

	@Override
	public void writeValue(NBTTagList nbt) {
		nbt.appendTag(new NBTTagShort(value));
	}

}
