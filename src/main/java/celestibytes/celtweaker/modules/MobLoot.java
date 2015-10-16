package celestibytes.celtweaker.modules;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import celestibytes.celtweaker.api.AModule;
import celestibytes.celtweaker.api.Tweak;

public class MobLoot extends AModule {
	
	public static class RandomLoot {
		public final ItemStack loot;
		public final float chance;
		
		public RandomLoot(ItemStack loot, float chance) {
			this.loot = loot;
			this.chance = chance;
		}
	}
	
	private static Map<Class<?>, List<RandomLoot>> loot = new HashMap<Class<?>, List<RandomLoot>>();
	private static Random rand = new Random();
	
	@SubscribeEvent
	public void onLivingDrops(LivingDropsEvent e) {
		if(!e.entityLiving.worldObj.isRemote) {
			List<RandomLoot> lst = loot.get(e.entityLiving.getClass());
			if(lst != null) {
//				System.out.println("Check loot!");
				Iterator<RandomLoot> iter = lst.iterator();
				while(iter.hasNext()) {
					RandomLoot rl = iter.next();
					
					float rf = rand.nextFloat();
//					System.out.println("rf=" + rf + " < rl.chance=" + rl.chance);
					if(rf < rl.chance) {
						ItemStack drop = rl.loot.copy();
						drop.stackSize += rand.nextInt(e.lootingLevel + 1);
						// System.out.println("add: " + rl.loot);
						
						e.drops.add(new EntityItem(e.entityLiving.worldObj, e.entityLiving.posX, e.entityLiving.posY, e.entityLiving.posZ, drop));
					}
				}
			}
		}
	}

	public MobLoot() {
		super("mobloot", typeList(String.class, ItemStack.class, Float.class));
		MinecraftForge.EVENT_BUS.register(this);
	}

	@Override
	public boolean apply(Tweak tweak) {
		String cls = (String) tweak.args[0];
		ItemStack drop = (ItemStack) tweak.args[1];
		float chance = (Float) tweak.args[2];
		
		try {
			Class<?> clazz = Class.forName(cls);
			List<RandomLoot> lst = loot.get(clazz);
			if(lst == null) {
				lst = new LinkedList<RandomLoot>();
				loot.put(clazz, lst);
			}
			
			lst.add(new RandomLoot(drop, chance));
		} catch(Exception e) {
			System.out.println("Couldn't add mobloot for: " + cls);
			e.printStackTrace();
			return false;
		}
		
		return true;
	}

	@Override
	public String[] getSamples() {
		return new String[] {
				"# Format: mobloot;class of the mob;loot stack;chance",
				"# Example: mobloot;\"net.minecraft.entity.monster.EntityZombie\";minecraft:diamond;0.8"
		};
	}

}
