package celestibytes.celtweaker.api.types;

import cpw.mods.fml.common.registry.GameData;
import net.minecraft.block.Block;

public class TBlock extends TBase {
	
	private static boolean init = false;
	private static Block defaultValue = null;
	
	public final Block value;

	public TBlock(String raw) {
		super(raw);
		if(!init) {
			defaultValue = GameData.getBlockRegistry().getDefaultValue();
			init = true;
		}
		
		this.value = GameData.getBlockRegistry().getObject(raw);
		if(value == defaultValue) {
			throw new IllegalArgumentException("Block of id \"" + raw + "\" not found!");
		}
	}
	
	@Override
	public String toString() {
		return "{block,raw=\"" + raw + "\"}";
	}

}
