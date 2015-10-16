package celestibytes.celtweaker.api;

import java.io.BufferedWriter;
import java.io.IOException;

import cpw.mods.fml.common.registry.GameData;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public abstract class AModule {
	
	public final String name;
	protected final Class<?>[] argTypes;
	private boolean undoable = false;
	
	public AModule(String name, Class<?>[] types) {
		this.name = name;
		this.argTypes = types;
	}
	
	protected static Class<?>[] typeList(Class<?>... types) {
		return types;
	}
	
	/** Return null for no call to handle() method */
	public Tweak checkArgs(final String[] args, final String cfgName, final int lineNumber) {
//		System.out.println("checkArgs");
//		Util.printStringArray(args);
		
		if(args.length == argTypes.length) {
			Object[] ret = new Object[args.length];
			for(int i = 0; i < args.length; i++) {
				
				String arg = args[i];
				Class<?> argType = argTypes[i];
				
				System.out.println("arg: " + arg);
				
				if(argType == Integer.class) {
					if(isInt(arg)) {
						ret[i] = Integer.parseInt(args[i]);
					} else {
						System.out.println("\tnotInt");
						return null;
					}
				} else if(argType == Boolean.class) {
					if(isBoolean(arg)) {
						ret[i] = arg.equalsIgnoreCase("true");
					} else {
						return null;
					}
				} else if(argType == Character.class) {
					if(isChar(arg)) {
						ret[i] = arg.charAt(1);
					} else {
						System.out.println("\tnotChar");
						return null;
					}
				} else if(argType == String.class) {
					if(isString(arg)) {
						ret[i] = ModuleUtil.unquote(args[i]);
					} else {
						System.out.println("\tnotString");
						return null;
					}
				} else if(argType == ItemStack.class) {
					ret[i] = parseItemStack(args[i]);
					if(ret[i] == null) {
						System.out.println("\tnotItemStack");
						return null;
					}
				} else if(argType == ModuleUtil.OreStringOrItemStack.class) {
					if(isOreString(arg)) {
						ret[i] = ModuleUtil.unquote(arg);
					} else if((ret[i] = parseItemStack(arg)) != null) {
						
					} else {
						System.out.println("\tnotOreString or ItemStack");
						return null;
					}
				} else if(argType == ModuleUtil.OreString.class) {
					if(isOreString(arg)) {
						String oreName = ModuleUtil.unquote(arg);
						System.out.println("oreName: " + oreName);
						ret[i] = oreName;
					} else {
						System.out.println("\tnotOre");
						return null;
					}
				} else if(argType == Float.class) {
					if(isFloat(args[i])) {
						ret[i] = Float.parseFloat(args[i]);
					} else {
						System.out.println("\tnotFloat");
						return null;
					}
				}
			}
			
			return new Tweak(this.name, cfgName, lineNumber, isUndoable(), ret);
		} else {
			System.out.println("expected " + argTypes.length + " arguments, got " + args.length);
		}
		
		return null;
	}
	
	private static boolean firstRun = true; 
	private static Block blc_none;
	private static Item ite_none;
	public ItemStack parseItemStack(String s) { // TODO: ore dict
		if(firstRun) {
			blc_none = GameData.getBlockRegistry().getDefaultValue();
			ite_none = GameData.getItemRegistry().getDefaultValue();
			firstRun = false;
		}
		
		String[] split = ModuleUtil.escapedSplit(s, ',');
		ModuleUtil.unescape(split);
		
//		System.out.println("parseStack");
//		Util.printStringArray(split);
		
		if(split.length == 0) {
			return null;
		}
		
		Block blc = null;
		Item ite = null;
		int count = 1;
		int metadata = 0;
		
		if(split.length >= 1) {
		
			blc = GameData.getBlockRegistry().getObject(split[0]);
			if(blc == blc_none) {
				ite = GameData.getItemRegistry().getObject(split[0]);
			}
			
			if(ite == ite_none && blc == blc_none) {
				return null;
			}
		}
		
		if(split.length >= 2) {
			try {
				count = Integer.parseInt(split[1]);
//				System.out.println("count: " + count);
			} catch(Exception e) {
				System.out.println("not int, count");
				return null;
			}
		}
		
		if(split.length >= 3) {
			try {
				metadata = Integer.parseInt(split[2]);
//				System.out.println("metadata: " + metadata);
			} catch(Exception e) {
				System.out.println("not int, meta");
				return null;
			}
		}
		
		return ite != ite_none ? new ItemStack(ite, count, metadata) : blc != blc_none ? new ItemStack(blc, count, metadata) : null;
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
			if(tweak.undoable) {
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
			bw.write("No samples, module returned null.\n");
		} else {
			for(int i = 0; i < samples.length; i++) {
				bw.write(samples[i] + "\n");
			}
		}
		
		bw.write("\n");
	}
	
}
