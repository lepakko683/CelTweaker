package celestibytes.celtweaker.api.types;

import cpw.mods.fml.common.registry.GameData;
import net.minecraft.item.Item;

public class TItem extends TBase {
	
	private static boolean init = false;
	private static Item defaultValue = null;
	
	public final Item value;

	public TItem(String raw) {
		super(raw);
		
		if(!init) {
			defaultValue = GameData.getItemRegistry().getDefaultValue();
			init = true;
		}
		
		this.value = GameData.getItemRegistry().getObject(raw);
		if(value == defaultValue) {
			throw new IllegalArgumentException("Item of id \"" + raw + "\" not found!");
		}
	}
	
	@Override
	public String toString() {
		return "{item,raw=\"" + raw + "\"}";
	}

}
