package celestibytes.celtweaker.api.types;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import celestibytes.celtweaker.api.CelTweakerException;

public class TBase {
	public final String raw;
	
	public TBase(String raw) {
		this.raw = raw;
//		System.out.println(this);
	}
	
	public String getRaw() {
		return raw;
	}
	
	public TString castString() {
		if(this instanceof TString) {
			return (TString) this;
		}
		
		throw new CelTweakerException("Invalid type: not TString. " + toString());
	}
	
	public TNumber castNumber() {
		if(this instanceof TNumber) {
			return (TNumber) this;
		}
		
		throw new CelTweakerException("Invalid type: not TNumber. " + toString());
	}
	
	public TBoolean castBoolean() {
		if(this instanceof TBoolean) {
			return (TBoolean) this;
		}
		
		throw new CelTweakerException("Invalid type: not TBoolean. " + toString());
	}
	
	public TBlock castBlock() {
		if(this instanceof TBlock) {
			return (TBlock) this;
		}
		
		throw new CelTweakerException("Invalid type: not TBlock. " + toString());
	}
	
	public TItem castItem() {
		if(this instanceof TItem) {
			return (TItem) this;
		}
		
		throw new CelTweakerException("Invalid type: not TItem. " + toString());
	}
	
	public TStack castStack() {
		if(this instanceof TStack) {
			return (TStack) this;
		}
		
		throw new CelTweakerException("Invalid type: not TStack. " + toString());
	}
	
	public TOre castOre() {
		if(this instanceof TOre) {
			return (TOre) this;
		}
		
		throw new CelTweakerException("Invalid type: not TOre. " + toString());
	}
	
	public TChar castChar() {
		if(this instanceof TChar) {
			return (TChar) this;
		}
		
		throw new CelTweakerException("Invalid type: not TChar. " + toString());
	}
	
	public TClass castClass() {
		if(this instanceof TClass) {
			return (TClass) this;
		}
		
		throw new CelTweakerException("Invalid type: not TClass. " + toString());
	}
	
	public static TBase parseType(String raw) {
		if(raw.length() == 0) {
			throw new CelTweakerException("Type string empty");
		}
		
		boolean bool = false;
		if(Character.isDigit(raw.charAt(0))) {
			return new TNumber(raw);
		} else if((bool = raw.equalsIgnoreCase("true")) || raw.equalsIgnoreCase("false")) {
			return new TBoolean(bool);
		} else {
			return new TString(raw);
		}
	}
	
	public boolean asBoolean() {
		if(this instanceof TBoolean) {
			return ((TBoolean)this).value;
		}
		
		throw new CelTweakerException("Invalid type: not TBoolean. " + toString());
	}
	
	public char asChar() {
		if(this instanceof TChar) {
			return ((TChar)this).value;
		}
		
		throw new CelTweakerException("Invalid type: not TChar. " + toString());
	}
	
	public Block asBlock() {
		if(this instanceof TBlock) {
			return ((TBlock)this).value;
		}
		
		throw new CelTweakerException("Invalid type: not TBlock. " + toString());
	}
	
	public Item asItem() {
		if(this instanceof TItem) {
			return ((TItem)this).value;
		}
		
		throw new CelTweakerException("Invalid type: not TItem. " + toString());
	}
	
	public byte asByte() {
		if(this instanceof TNumber) {
			return (byte) ((TNumber)this).value;
		}
		
		throw new CelTweakerException("Invalid type: not TNumber. " + toString());
	}
	
	public short asShort() {
		if(this instanceof TNumber) {
			return (short) ((TNumber)this).value;
		}
		
		throw new CelTweakerException("Invalid type: not TNumber. " + toString());
	}
	
	public int asInt() {
		if(this instanceof TNumber) {
			return (int) ((TNumber)this).value;
		}
		
		throw new CelTweakerException("Invalid type: not TNumber. " + toString());
	}
	
	public long asLong() {
		if(this instanceof TNumber) {
			return (long) ((TNumber)this).value;
		}
		
		throw new CelTweakerException("Invalid type: not TNumber. " + toString());
	}
	
	public float asFloat() {
		if(this instanceof TNumber) {
			return (float) ((TNumber)this).value;
		}
		
		throw new CelTweakerException("Invalid type: not TNumber. " + toString());
	}
	
	public double asDouble() {
		if(this instanceof TNumber) {
			return (double) ((TNumber)this).value;
		}
		
		throw new CelTweakerException("Invalid type: not TNumber. " + toString());
	}
	
	public String asOre() {
		if(this instanceof TOre) {
			return raw;
		}
		
		throw new CelTweakerException("Invalid type: not TOre. " + toString());
	}
	
	public String asString() {
		if(this instanceof TString) {
			return raw;
		}
		
		throw new CelTweakerException("Invalid type: not TString. " + toString());
	}
	
	public ItemStack asItemStack() {
		if(this instanceof TStack) {
			return ((TStack)this).value;
		}
		
		throw new CelTweakerException("Invalid type: not TStack. " + toString());
	}
	
	public Class<?> asClass() {
		if(this instanceof TClass) {
			return ((TClass)this).clazz;
		}
		
		throw new CelTweakerException("Invalid type: not TClass. " + toString());
	}
	
	public boolean checkClassExact(Class<? extends TBase> cls) {
		return this.getClass() == cls;
	}
	
	public boolean instanceOf(Class<? extends TBase> cls) {
		return cls.isAssignableFrom(this.getClass());
	}
	
	@Override
	public String toString() {
		return "{raw=\"" + raw + "\"}";
	}

}
