package celestibytes.celtweaker;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.regex.Pattern;

import cpw.mods.fml.common.registry.GameData;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public abstract class AHandlerModule {
	
	public final String name;
	protected final Class<?>[] argTypes;
	
	public AHandlerModule(String name, Class<?>[] types) {
		this.name = name;
		this.argTypes = types;
	}
	
	protected static Class<?>[] typeList(Class<?>... types) {
		return types;
	}
	
	/** Return null for no call to handle() method */
	public Object[] checkArgs(String[] args) {
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
						ret[i] = Util.unquote(args[i]);
					} else {
						System.out.println("\tnotString");
						return null;
					}
				} else if(argType == ItemStack.class) { // split[0].matches("\".*\"")
					ret[i] = parseItemStack(args[i]);
					if(ret[i] == null) {
						System.out.println("\tnotItemStack");
						return null;
					}
				} else if(argType == Util.OreStringOrItemStack.class) {
					if(isOreString(arg)) {
//						ret[i] = new Util.OreString(Util.unquote(arg));
						ret[i] = Util.unquote(arg);
					} else if((ret[i] = parseItemStack(arg)) != null) {
						
					} else {
						System.out.println("\tnotOreString or ItemStack");
						return null;
					}
				} else if(argType == Util.OreString.class) {
					if(isOreString(arg)) {
						String oreName = Util.unquote(arg);
						System.out.println("oreName: " + oreName);
//						ret[i] = new Util.OreString(oreName);
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
			
			return ret;
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
		
		String[] split = Util.escapedSplit(s, ',');
		Util.unescape(split);
		
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
		String[] split = Util.escapedSplit(s, ',');
		
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
	
	public abstract void handle(Object[] args);
	
	public abstract String[] getSamples();
	
	public void writeSamples(BufferedWriter bw) throws IOException {
		bw.write("# Module: " + name + "\n");
		
		String[] samples = getSamples();
		for(int i = 0; i < samples.length; i++) {
			bw.write(samples[i] + "\n");
		}
		
		bw.write("\n");
	}
	
}
