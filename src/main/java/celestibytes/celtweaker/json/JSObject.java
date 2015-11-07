package celestibytes.celtweaker.json;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public abstract class JSObject {
	protected static final int NBT_BYTE = 1;
	protected static final int NBT_SHORT = 2;
	protected static final int NBT_INT = 3;
	protected static final int NBT_LONG = 4;
	protected static final int NBT_FLOAT = 5;
	protected static final int NBT_DOUBLE = 6;
	protected static final int NBT_STRING = 7;
	protected static final int NBT_MULTI = 8;
	protected static final int NBT_ARRAY = 9;
	
	public final int type;
	public String key;
	
	public JSObject(int type, String key) {
		this.type = type;
		this.key = key;
	}
	
	public abstract void writeValue(NBTTagCompound nbt);
	public abstract void writeValue(NBTTagList nbt);
	
}
