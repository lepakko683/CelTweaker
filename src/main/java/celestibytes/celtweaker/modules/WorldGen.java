package celestibytes.celtweaker.modules;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.terraingen.OreGenEvent;
import celestibytes.celtweaker.api.AModule;
import celestibytes.celtweaker.api.CelTweakerException;
import celestibytes.celtweaker.api.Tweak;
import celestibytes.celtweaker.api.Version;
import celestibytes.celtweaker.api.types.TBase;


public class WorldGen extends AModule {
	
	public static abstract class GenWrapper {
		
		public final int dim;
		
		public GenWrapper(int dim) {
			this.dim = dim;
		}
	}
	
	public static class OreGenWrapper extends GenWrapper {
		
		public final int loops, ymin, ymax;
		public final WorldGenMinable gen;
		
		public OreGenWrapper(int dim, int loops, WorldGenMinable gen, int ymin, int ymax) {
			super(dim);
			
			this.loops = loops;
			this.gen = gen;
			this.ymin = ymin;
			this.ymax = ymax;
		}
		
		public void generate(OreGenEvent event) {
			for(int l = 0; l < loops; ++l) {
				int x = event.worldX + event.rand.nextInt(16);
				int y = event.rand.nextInt(ymax - ymin) + ymin;
				int z = event.worldZ + event.rand.nextInt(16);
				
				gen.generate(event.world, event.rand, x, y, z);
			}
		}
	}
	
	public static abstract class SubModule {
		public final String name;
		
		public SubModule(String name) {
			this.name = name;
		}
		
		public abstract Object[] checkArgs(TBase[] args);
		
		public abstract boolean apply(Tweak tweak);
	}
	
	private static Map<String, SubModule> submodules = new HashMap<String, SubModule>();
	private static List<OreGenWrapper> oreGenerators = new LinkedList<OreGenWrapper>();
	
	@SubscribeEvent
	public void onOreGen(OreGenEvent event) {
		for(OreGenWrapper ore : oreGenerators) {
			ore.generate(event);
		}
	}
	
	private void registerSubmodule(SubModule sub) {
		if(!submodules.containsKey(sub.name)) {
			submodules.put(sub.name, sub);
		} else {
			System.out.println("Attempted to register submodule twice: " + sub.name);
		}
	}

	public WorldGen() {
		super("worldgen", new Version(0, 1), null);
		
		registerSubmodule(new SubModule("ore") { // dim, loops, ymin, ymax, block, meta, target
			
			@Override
			public Object[] checkArgs(TBase[] args) {
				if(args.length >= 7 && args.length <= 8) {
					Object[] ret = new Object[args.length];
					
					ret[0] = args[1].asInt();   // dim
					ret[1] = args[2].asInt();   // loops
					ret[2] = args[3].asInt();   // number
					ret[3] = args[4].asInt();   // ymin
					ret[4] = args[5].asInt();   // ymax
					ret[5] = args[6].asBlock(); // block
					ret[6] = args[7].asInt();   // meta
					
					if(args.length == 9) {
						ret[7] = args[8].asBlock();   // target
					}
					
					return ret;
				}
				
				return null;
			}
			
			@Override
			public boolean apply(Tweak tweak) {
				int dim = (Integer) tweak.args[0];
				int loops = (Integer) tweak.args[1];
				int number = (Integer) tweak.args[2];
				int ymin = (Integer) tweak.args[3];
				int ymax = (Integer) tweak.args[4];
				Block block = (Block) tweak.args[5];
				int meta = (Integer) tweak.args[6];
				Block target = tweak.args.length >= 8 ? (Block) tweak.args[7] : null;
				
				oreGenerators.add(new OreGenWrapper(dim, loops, new WorldGenMinable(block, meta, number, target == null ? Blocks.stone : target), ymin, ymax));
				return true;
			}
		});
		
		MinecraftForge.ORE_GEN_BUS.register(this);
	}
	
	@Override
	public Tweak checkArgs(TBase[] args, String cfgName, int tweakidx) {
		String subname = args[0].asString().toLowerCase(Locale.ENGLISH);
		SubModule sub = submodules.get(subname);
		
		if(sub != null) {
			Object[] ret = sub.checkArgs(args);
			if(ret != null) {
				Tweak twe = new Tweak(this, cfgName, tweakidx, ret);
				twe.customData = sub;
				
				return twe;
			}
			
			throw new CelTweakerException("Invalid arguments for submodule: " + subname);
		}
		
		throw new CelTweakerException("No such submodule: " + subname);
	}

	@Override
	public boolean apply(Tweak tweak) {
		if(tweak.customData != null && tweak.customData instanceof SubModule) {
			SubModule sub = (SubModule) tweak.customData;
			return sub.apply(tweak);
		}
		
		return false;
	}

	@Override
	public String[] getSamples() {
		return null;
	}
	
}
