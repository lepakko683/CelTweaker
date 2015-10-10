package celestibytes.celtweaker.modules;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapedOreRecipe;
import celestibytes.celtweaker.AHandlerModule;
import celestibytes.celtweaker.Util;

public class VanillaCrafting extends AHandlerModule {

	public VanillaCrafting() {
		super("vanillacrafting", null);
	}
	
	@Override
	public Object[] checkArgs(String[] args) {
		Object[] ret = new Object[args.length];
		int recipeHeight = 0;
		
		for(int i=0;isString(args[i]); i++) {
			ret[i] = Util.unquote(args[i]);
			recipeHeight++;
		}
		
		for(int i = recipeHeight + 1; i < args.length; i += 2) {
			if(isChar(args[i - 1])) {
				ret[i - 1] = Character.valueOf(args[i - 1].charAt(1));
				if(isOreString(args[i])) {
					ret[i] = Util.unquote(args[i]);
				} else {
					ItemStack stack = parseItemStack(args[i]);
					if(stack == null) {
						System.out.println("stack null");
						return null;
					}
					
					ret[i] = stack;
				}
			} else {
				System.out.println("not char! + ");
				return null;
			}
		}
		
		ItemStack result = parseItemStack(args[args.length - 1]);
		if(result == null) {
			System.out.println("result null");
			return null;
		}
		
		ret[ret.length - 1] = result;
		
		return ret;
	}

	@Override
	public void handle(Object[] args) {
		int shapeHeight = 0;
		for(int i = 0; args[i] instanceof String; i++) {
			System.out.println("> " + args[i]);
			shapeHeight++;
		}
		
		for(int i = shapeHeight + 1; i < args.length; i += 2) {
			System.out.println("'" + args[i - 1] + "' = " + args[i]);
		}
		
//		System.out.println("result=" + args[args.length - 1]);
		
		Object[] recipe = new Object[args.length - 1];
		System.arraycopy(args, 0, recipe, 0, recipe.length);
		
		System.out.println("Adding recipe...");
		GameRegistry.addRecipe(new ShapedOreRecipe((ItemStack) args[args.length - 1], recipe));
	}

	@Override
	public String[] getSamples() {
		return new String[]{
				"# Format: vanillacrafting;shape;shape;shape;key;value;key;value;result",
				"#     Key is a single ASCII character. And value is either an itemstack or an ore dictionary name.",
				"# Example: vanillacrafting;\"DD\";\"II\";'D';minecraft:dirt;'I';minecraft:iron_ingot;minecraft:diamond,2"
		};
	}

}
