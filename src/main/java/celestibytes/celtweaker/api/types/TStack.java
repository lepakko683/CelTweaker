package celestibytes.celtweaker.api.types;

import cpw.mods.fml.common.registry.GameData;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class TStack extends TBase {
	
	private static boolean init = false;
	private static Item defaultItem = null;
	private static Block defaultBlock = null;
	
	public final ItemStack value;

	/** Don't use this constructor */
	public TStack(String raw) {
		super(raw);
		value = null;
	}
	
	public TStack(String id, int count, int metadata) {
		super(id);
		if(!init) {
			defaultItem = GameData.getItemRegistry().getDefaultValue();
			defaultBlock = GameData.getBlockRegistry().getDefaultValue();
			init = true;
		}
		
		Item item = GameData.getItemRegistry().getObject(id);
		Block block = null;
		if(item == defaultItem) {
			block = GameData.getBlockRegistry().getObject(id);
		}
		
		if(block == defaultBlock) {
			throw new IllegalArgumentException("Invalid itemid");
		}
		
		if(item != defaultItem) {
			value = new ItemStack(item, count, metadata);
		} else {
			value = new ItemStack(block, count, metadata);
		}
	}
	
	public TStack(TItem item, int count, int metadata) {
		super(item.raw);
		
		value = new ItemStack(item.value, count, metadata);
	}
	
	public TStack(TBlock block, int count, int metadata) {
		super(block.raw);
		
		value = new ItemStack(block.value, count, metadata);
	}
	
	@Override
	public String toString() {
		return "{stack,raw=\"" + raw + "\"}";
	}

}
