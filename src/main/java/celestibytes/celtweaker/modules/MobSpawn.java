package celestibytes.celtweaker.modules;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.world.biome.BiomeGenBase;
import cpw.mods.fml.common.registry.EntityRegistry;
import celestibytes.celtweaker.api.AModule;
import celestibytes.celtweaker.api.Tweak;
import celestibytes.celtweaker.api.Version;
import celestibytes.celtweaker.api.types.TNumber;
import celestibytes.celtweaker.api.types.TString;

public class MobSpawn extends AModule {
	
	private Map<String, WeakReference<BiomeGenBase>> biomeCache = new HashMap<String, WeakReference<BiomeGenBase>>();

	public MobSpawn() { // entityClass, spawnWeight, minGroupSize, maxGroupSize, creatureType, biomeName
		super("mobspawn", new Version(0, 1), typeList(Class.class, Integer.class, Integer.class, Integer.class, String.class, String.class));
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean apply(Tweak tweak) {
		Class<?> mob = (Class<?>) tweak.args[0];
		Class<? extends EntityLiving> mobCls = null;
		if(EntityLiving.class.isAssignableFrom(mob)) {
			mobCls = (Class<? extends EntityLiving>) mob;
		}
		 
		int weight = (Integer) tweak.args[1];
		int minGroupSize = (Integer) tweak.args[2];
		int maxGroupSize = (Integer) tweak.args[3];
		String creatureType = (String) tweak.args[4];
		String biomeName = (String) tweak.args[5];
		
		EnumCreatureType creType = EnumCreatureType.valueOf(creatureType);
		if(creType == null) {
			return false;
		}
		
		BiomeGenBase[] biomes = BiomeGenBase.getBiomeGenArray();
		for(int i = 0; i < biomes.length; i++) {
			BiomeGenBase biome = biomes[i];
			if(biomeName.equalsIgnoreCase(biome.biomeName)) {
				biomeCache.put(biomeName, new WeakReference<BiomeGenBase>(biome));
				EntityRegistry.addSpawn(mobCls, weight, minGroupSize, maxGroupSize, creType, biome);
				return true;
			}
		}
		
		return false;
	}

	@Override
	public String[] getSamples() {
		return new String[]{
				"# Mobspawn"
		};
	}

}
