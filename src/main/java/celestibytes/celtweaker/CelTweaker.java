package celestibytes.celtweaker;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import celestibytes.celtweaker.api.AModule;
import celestibytes.celtweaker.api.ILogger;
import celestibytes.celtweaker.api.IModuleProvider;
import celestibytes.celtweaker.api.ITweakHandler;
import celestibytes.celtweaker.api.Tweak;
import celestibytes.celtweaker.api.ModuleUtil;
import celestibytes.celtweaker.api.types.TBase;
import celestibytes.celtweaker.api.types.TString;
import celestibytes.celtweaker.json.JSObject;
import celestibytes.celtweaker.mods.AMT2;
import celestibytes.celtweaker.mods.Botania;
import celestibytes.celtweaker.mods.Minecraft;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInterModComms;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.relauncher.Side;

@Mod(modid=Ref.MODID, name=Ref.MOD_NAME, version=Ref.VERSION)
public class CelTweaker {
	
	// IMC key: CelTweaker-register-provider
	
	private List<IModuleProvider> providers = new LinkedList<IModuleProvider>();
	private Map<String, AModule> handlers = new HashMap<String, AModule>();
	
	private void registerProvider(IModuleProvider prov) {
		providers.add(prov);
		System.out.println("Registered Module Provider: " + prov.getProviderName());
	}
	
	private boolean registerModule(AModule h) {
		if(handlers.containsKey(h.name)) {
			System.out.println("Handler already exists!");
			return false;
		} else {
			handlers.put(h.name, h);
			return true;
		}
	}
	
	private boolean isProviderEnabled(IModuleProvider prov) {
		return true; // TODO: allow providers/mods to be disabled by the user
	}
	
	@EventHandler
	public void imcHandler(FMLInterModComms.IMCEvent e) {
		for(FMLInterModComms.IMCMessage msg : e.getMessages()) {
			if(msg.key.equalsIgnoreCase("CelTweaker-register-provider")) {
				if(msg.isStringMessage()) {
					String str = msg.getStringValue();
					if(str != null && str.length() > 0) {
						try {
							Class<?> clazz = Class.forName(str);
							Object inst = clazz.newInstance();
							
							if(inst instanceof IModuleProvider) {
								registerProvider((IModuleProvider) inst);
							}
						} catch(Exception ex) {
							ex.printStackTrace();
						}
					}
				}
			}
			
		}
	}
	
	private void registerProviders() {
		registerProvider(new Minecraft());
		registerProvider(new Botania());
		registerProvider(new AMT2());
	}
	
	private void registerModules() {
		for(IModuleProvider prov : providers) {
			if(prov.shouldBeLoaded() && isProviderEnabled(prov)) {
				int count = 0;
				for(AModule module : prov.getModules()) {
					if(registerModule(module)) {
						count++;
					}
				}
				
				System.out.println("Registered " + count + " modules from " + prov.getProviderName());
			} else {
				System.out.println("Module Provider \"" + prov.getProviderName() + "\" was disabled");
			}
		}
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent e) {
		registerProviders();
		registerModules();
		
		File ctRoot = null;
		File cfgDir = null;
		File moduleDir = null;
		
		try {
			File saves = FMLCommonHandler.instance().getSavesDirectory();
			File mcroot = null;
			
			if(FMLCommonHandler.instance().getSide() == Side.CLIENT) {
				mcroot = saves.getParentFile();
			} else {
				mcroot = saves;
			}
			
			ctRoot = new File(mcroot, "CelTweaker");
			cfgDir = new File(ctRoot, "configs");
			moduleDir = new File(ctRoot, "modules");
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		
		if(ctRoot == null || cfgDir == null || moduleDir == null) {
			return;
		}
		
		if(ctRoot.isFile()) {
			System.out.println("File at " + ctRoot.getAbsolutePath() + " is blocking the creation of CelTweaker directory. Rename or delete it to continue.");
			return;
		}
		
		if(cfgDir.isFile()) {
			System.out.println("File at " + cfgDir.getAbsolutePath() + " is blocking the creation of config directory. Rename or delete it to continue.");
			return;
		}
		
		if(moduleDir.isFile()) {
			System.out.println("File at " + moduleDir.getAbsolutePath() + " is blocking the creation of module directory. Rename or delete it to continue.");
			return;
		}
		
		if(!ctRoot.isDirectory()) {
			if(!ctRoot.mkdirs()) {
				System.out.println("Error while creating CelTweaker directory: " + ctRoot.getAbsolutePath());
				return;
			}
		}
		
		if(!cfgDir.isDirectory()) {
			if(!cfgDir.mkdirs()) {
				System.out.println("Error while creating config directory: " + cfgDir.getAbsolutePath());
				return;
			}
		}
		
		if(!moduleDir.isDirectory()) {
			if(!moduleDir.mkdirs()) {
				System.out.println("Error while creating module directory: " + moduleDir.getAbsolutePath());
				return;
			}
		}
		
		/* Disabled, re-enable when up to date
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(new File(cfgDir, "sample.cfg")));
			bw.write("# Version: " + Ref.VERSION + "\n");
			bw.write("# Sample CelTweaker cfg file. This file was generated automatically\n");
			bw.write("# and will not be regenerated unless you delete the directory. You\n");
			bw.write("# are free to delete this file.\n\n");
			
			bw.write("# Basic format:\n");
			bw.write("#     moduleName;arg1;arg2;arg3 etc\n\n");
			
			bw.write("# Supported datatypes:\n");
			bw.write("#     ItemStack: prefix:itemid,count,metadata e.g. minecraft:diamond,1,0\n");
			bw.write("#     OreDict name: <name> e.g. <stickWood>\n");
			bw.write("#     String: \"text\"\n");
			bw.write("#     Integer: 1337\n");
			bw.write("#     Character: 'C'\n");
			bw.write("#     Float: 3.14\n\n");
			bw.write("#     Boolean: true, false\n\n");
			
			bw.write("# Installed modules:\n\n");
			
			Iterator<AModule> modIter = handlers.values().iterator();
			while(modIter.hasNext()) {
				AModule mod = modIter.next();
				mod.writeSamples(bw);
			}
			
			bw.close();
		} catch(Exception ex) {
			System.out.println("Error while generating sample cfg file.");
			ex.printStackTrace();
		}
		*/
		
//		System.out.println("cfg dir: " + configDir.getAbsolutePath());
		
		FastParser parser = new FastParser();
		TweakHandler th = new TweakHandler();
		Logger log = new Logger("CelTweaker");
		
		File[] cfgFiles = cfgDir.listFiles(new FileFilter() {
			
			@Override
			public boolean accept(File file) {
				if(file.isFile() && file.getName().toLowerCase().endsWith(".cfg")) {
					return true;
				}
				
				return false;
			}
		});
		
		for(File cfg : cfgFiles) {
			try {
				FileReader fr = new FileReader(cfg);
				
				parser.parseTweaks(fr, th, log);
				
				fr.close();
			} catch(Exception ex) {
				System.out.println("Error while parsing file: " + cfg.getAbsolutePath());
				ex.printStackTrace();
			}
		}
	}
	
	public class TweakHandler implements ITweakHandler {

		@Override
		public Tweak handleTweak(List<TBase> args, ILogger log) {
			TString name = args.get(0).castString();
			if(name != null) {
				AModule module = handlers.get(name.asString());
				if(module == null) {
					log.err("Module \"" + name.asString() + "\" not found.");
				} else {
					LinkedList<TBase> largs = new LinkedList<TBase>(args);
					largs.removeFirst();
					Tweak tweak = module.checkArgs(largs.toArray(new TBase[0]), "someCfg", -1); // TODO: use proper cfgName and index
					if(module.apply(tweak)) {
						System.out.println("Successfully enabled tweak: " + tweak);
					} else {
						System.out.println("Failed to enable tweak: " + tweak);
					}
				}
			} else {
				log.err("Module name not string!");
			}
			
			return null;
		}
		
	}
}
