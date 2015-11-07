package celestibytes.celtweaker.api;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import celestibytes.celtweaker.api.types.TBase;
import celestibytes.celtweaker.api.types.TBoolean;
import celestibytes.celtweaker.api.types.TChar;
import celestibytes.celtweaker.api.types.TClass;
import celestibytes.celtweaker.api.types.TNumber;
import celestibytes.celtweaker.api.types.TOre;
import celestibytes.celtweaker.api.types.TStack;
import celestibytes.celtweaker.api.types.TString;
import cpw.mods.fml.common.registry.GameData;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.oredict.OreDictionary;

public abstract class AModule {
	
	public static enum ValueType {
		BYTE,
		SHORT,
		INT,
		LONG,
		FLOAT,
		DOUBLE,
		STRING;
	}
	
	public static class Tuple<A, B> {
		public A a;
		public B b;
		
		public Tuple(A a, B b) {
			this.a = a;
			this.b = b;
		}
	}
	
	public interface IMArg {
		// nothing?
	}
	
	public static class ArgOr implements IMArg {
		
		public final Class<? extends TBase>[] options;
		
		public ArgOr(Class<? extends TBase>... options) {
			this.options = options;
		}
		
		public boolean check(TBase arg) {
			for(int i = 0; i < options.length; i++) {
				if(arg.getClass() == options[i]) {
					return true;
				}
			}
			
			return false;
		}
	}
	
//	public static class ArgOpt implements IMArg {
//		public final Class<? extends TBase> opt;
//		
//		public ArgOpt(Class<? extends TBase> opt) {
//			this.opt = opt;
//		}
//		
//		public boolean check(TBase arg) {
//			return arg.getClass() == opt;
//		}
//	}
	
	public static class ArgRepeat implements IMArg {
		public final IMArg[] list;
		public final int min, max;
		
		public ArgRepeat(IMArg... list) {
			this.list = list;
			min = 1;
			max = Integer.MAX_VALUE;
		}
		
		public ArgRepeat(int min, int max, IMArg... list) {
			this.list = list;
			this.min = min;
			this.max = max;
		}
		
		/** index = index of first arg to check, return = number of checked elements */
		public int check(TBase[] args, int index, List<TBase> ret) {
			int checks = 0;
			for(int i = index; i < max; i += list.length) {
				
				for(int q = 0; q < list.length; q++) {
					TBase arg = i + q < args.length ? args[i + q] : null;
					IMArg need = list[q];
					
					if(arg == null) {
						return -1;
					} else if(need instanceof TBase) {
						if(need.getClass().isAssignableFrom(arg.getClass())) {
							ret.add(arg);
						}
					} else if(need instanceof ArgOr) {
						if(((ArgOr)need).check(arg)) {
							ret.add(arg);
						} else {
							return checks >= min ?  : -1;
						}
					} else {
						return -1;
					}
				}
				
				checks++;
				if(checks >= min) {
					return i;
				}
			}
			
			return -1;
		}
		
	}
	
	public final String name;
	public final Version version;
	protected final Class<?>[] argTypes;
	private boolean undoable = false;
	
	public AModule(String name, Version version, Class<?>[] types) {
		this.name = name;
		this.version = version;
		this.argTypes = types;
	}
	
	protected static Class<?>[] typeList(Class<?>... types) {
		return types;
	}
	
	public Tweak checkArgs(final TBase[] args, final String cfgName, final int tweakidx) {
		if(argTypes.length != args.length) {
			return null;
		}
		
		List<TBase> argv = new ArrayList<TBase>();
		
		for(int i = 0; i < args.length; i++) {
			TBase n = args[i];
			Class<?> needType = argTypes[i];
			
			try {
				if(needType == TString.class) {
					argv.add(n.castString());
				} else if(needType == TBoolean.class) {
					argv.add(n.castBoolean());
				} else if(TNumber.class.isAssignableFrom(needType) && needType.isAssignableFrom(n.getClass())) {
					argv.add(n);
				} else if(needType == TChar.class) {
					argv.add(n.castChar());
				} else if(needType == TStack.class) {
					argv.add(n.castStack());
				} else if(needType == TOre.class) {
					argv.add(n.castOre());
				} else if(needType == TClass.class) {
					argv.add(n.castClass());
				} else {
					return null;
				}
				
			} catch(Exception e) {
				e.printStackTrace();
				return null;
			}
		}
		
		return new Tweak(this, cfgName, tweakidx, argv.toArray(new TBase[0]));
	}
	
	private static boolean firstRun = true; 
	private static Block blc_none;
	private static Item ite_none;
	public ItemStack parseItemStack(String s) {
		if(firstRun) {
			blc_none = GameData.getBlockRegistry().getDefaultValue();
			ite_none = GameData.getItemRegistry().getDefaultValue();
			firstRun = false;
		}
		
		String[] split = ModuleUtil.escapedSplit(s, ',');
//		ModuleUtil.unescape(split);
		
//		System.out.println("parseStack");
//		Util.printStringArray(split);
		
		if(split.length == 0) {
			return null;
		}
		
		Block blc = null;
		Item ite = null;
		int count = 1;
		int metadata = 0;
		
		int nbtStart = 0;
		
		if(split.length >= 1) {
			nbtStart = 1;
			String itemid = ModuleUtil.unescape(split[0], '\\');
			
			blc = GameData.getBlockRegistry().getObject(itemid);
			if(blc == blc_none) {
				ite = GameData.getItemRegistry().getObject(itemid);
			}
			
			if(ite == ite_none && blc == blc_none) {
				return null;
			}
		}
		
		if(split.length >= 2) {
			nbtStart = 2;
			String countStr = ModuleUtil.unescape(split[1], '\\');
			
			try {
				count = Integer.parseInt(countStr);
//				System.out.println("count: " + count);
			} catch(Exception e) {
				System.out.println("not int, count");
				return null;
			}
		}
		
		if(split.length >= 3) {
			nbtStart = 3;
			String metadataStr = ModuleUtil.unescape(split[2], '\\');
			
			try {
				metadata = Integer.parseInt(metadataStr);
//				System.out.println("metadata: " + metadata);
			} catch(Exception e) {
				System.out.println("not int, meta");
				return null;
			}
		}
		
		ItemStack ret = ite != ite_none ? new ItemStack(ite, count, metadata) : blc != blc_none ? new ItemStack(blc, count, metadata) : null;
		if(ret != null) {
			NBTTagCompound nbt = new NBTTagCompound();
			
			for(int i = nbtStart; i < split.length; i++) {
				Tuple<String[], ValueType> spl = specialQuotedSplit(split[i]);
				addNBTTag(nbt, spl.a, spl.b);
			}
		}
		
		return ret;
	}
	
	private boolean addNBTTag(NBTTagCompound nbt, String[] data, ValueType type) {
		try {
			NBTTagCompound node = nbt;
			for(int i = 0; i < data.length; i++) {
				if(i == data.length - 1) {
					if(type == ValueType.BYTE) {
						node.setByte(data[data.length - 2], Byte.parseByte(data[data.length - 1]));
					} else if(type == ValueType.SHORT) {
						node.setShort(data[data.length - 2], Short.parseShort(data[data.length - 1]));
					} else if(type == ValueType.INT) {
						node.setInteger(data[data.length - 2], Integer.parseInt(data[data.length - 1]));
					} else if(type == ValueType.LONG) {
						node.setLong(data[data.length - 2], Long.parseLong(data[data.length - 1]));
					} else if(type == ValueType.FLOAT) {
						node.setFloat(data[data.length - 2], Float.parseFloat(data[data.length - 1]));
					} else if(type == ValueType.DOUBLE) {
						node.setDouble(data[data.length - 2], Double.parseDouble(data[data.length - 1]));
					} else if(type == ValueType.STRING) {
						node.setString(data[data.length - 2], data[data.length - 1]);
					} else {
						return false;
					}
				} else {
					if(node.hasKey(data[i], 10)) {
						node = node.getCompoundTag(data[i]);
					} else {
						NBTTagCompound next = new NBTTagCompound();
						node.setTag(data[i], next);
						node = next;
					}
				}
			}
		} catch(Exception e) {
			e.printStackTrace(); // ?
			return false;
		}
		
		return true;
	}
	
	public static Tuple<String[], ValueType> specialQuotedSplit(String tag) {
		List<String> parts = new LinkedList<String>();
		
		int taglen = tag.length();
		char[] str = tag.toCharArray();
		
		int partStart = 0;
		boolean readingQuote = false;
		boolean readingValue = false;
		ValueType valType = null;
		for(int i = 0; i < taglen; i++) {
			if(readingValue) {
				if(valType == null) {
					char type = str[i];
					if(type == 'b') {
						valType = ValueType.BYTE;
					} else if(type == 's') {
						valType = ValueType.SHORT;
					} else if(type == 'i') {
						valType = ValueType.INT;
					} else if(type == 'l') {
						valType = ValueType.LONG;
					} else if(type == 'f') {
						valType = ValueType.FLOAT;
					} else if(type == 'd') {
						valType = ValueType.DOUBLE;
					} else if(type == '"') {
						valType = ValueType.STRING;
					} else {
						return null;
					}
				} else {
					if(i == taglen - 1) {
						if(valType == ValueType.STRING) {
							if(str[i] != '"') {
								return null;
							} else {
								parts.add(ModuleUtil.unescape(tag.substring(partStart + 1, i), '\\'));								
							}
						} else {
							parts.add(tag.substring(partStart + 1, taglen));
						}
					}
				}
			} else if(readingQuote) {
				if(str[i] == '"' && str[i - 1] != '\\') {
					parts.add(ModuleUtil.unescape(tag.substring(partStart, i), '\\'));
					readingQuote = false;
				}
			} else {
				if(str[i] == '"') {
					readingQuote = true;
					partStart = i + 1;
				} else if(str[i] == '=') {
					readingValue = true;
					partStart = i + 1;
				}
			}
		}
		
		if(readingQuote) {
			return null;
		}
		
		return new Tuple<String[], ValueType>(parts.toArray(new String[0]), valType);
	}
	
	public boolean isString(String s) {
		return s.charAt(0) == '"' && s.charAt(s.length() - 1) == '"';
	}
	
	public boolean isInt(String s) {
		try {
			Integer.parseInt(s);
			return true;
		} catch(Exception e) {
			
		}
		
		return false;
	}
	
	public boolean isFloat(String s) {
		try {
			Float.parseFloat(s);
			return true;
		} catch(Exception e) {
			
		}
		
		return false;
	}
	
	public boolean isBoolean(String s) {
		return s.equalsIgnoreCase("true") || s.equalsIgnoreCase("false");
	}
	
	public boolean isChar(String s) {
		return s.length() == 3 && s.charAt(0) == '\'' && s.charAt(2) == '\'';
	}
	
	public boolean isOreString(String s) {
		return s.length() > 2 && s.charAt(0) == '<' && s.charAt(s.length() - 1) == '>';
	}
	
	public boolean isItemStack(String s) {
		String[] split = ModuleUtil.escapedSplit(s, ',');
		
		if(!isString(split[0])) {
			return false;
		}
		
		if(split.length >= 2) {
			if(!isInt(split[1])) {
				return false;
			}
		}
		
		if(split.length >= 3) {
			if(!isInt(split[2])) {
				return false;
			}
		}
		
		return split.length > 3 ? false : true;
	}
	
	public boolean doesOreStringValid(String ore) {
		return OreDictionary.doesOreNameExist(ore.substring(1, ore.length() - 1));
	}
	
	protected void setUndoable(boolean undoable) {
		this.undoable = undoable;
	}
	
	public final boolean tweakApply(Tweak tweak) {
		if(!tweak.enabled) {
			if(apply(tweak)) {
				tweak.enabled = true;
				return true;
			} else {
				System.out.println("Error: failed to apply tweak!");
			}
		} else {
			System.out.println("Error: trying to apply already enabled tweak!");
		}
		
		return false;
	}
	
	public final boolean tweakUndo(Tweak tweak) {
		if(tweak.enabled) {
			if(tweak.isUndoable()) {
				if(undo(tweak)) {
					tweak.enabled = false;
					return true;
				} else {
					System.out.println("Error: failed to undo tweak!");
				}
			} else {
				System.out.println("Error: trying to undo un-undoable tweak!");
			}
		} else {
			System.out.println("Error: trying to undo unenabled tweak!");
		}
		
		return false;
	}
	
	public abstract boolean apply(Tweak tweak);
	
	public boolean undo(Tweak tweak) {
		return false;
	}
	
	public final boolean isUndoable() {
		return undoable;
	}
	
	public abstract String[] getSamples();
	
	public void writeSamples(BufferedWriter bw) throws IOException {
		bw.write("# Module: " + name + "\n");
		
		String[] samples = getSamples();
		if(samples == null) {
			bw.write("# No samples, module returned null.\n");
		} else {
			for(int i = 0; i < samples.length; i++) {
				bw.write(samples[i] + "\n");
			}
		}
		
		bw.write("\n");
	}
	
}
