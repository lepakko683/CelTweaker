package celestibytes.celtweaker;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import celestibytes.celtweaker.api.AModule;
import celestibytes.celtweaker.api.IModuleProvider;
import celestibytes.celtweaker.api.Tweak;
import celestibytes.celtweaker.api.ModuleUtil;
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
	
	private void registerModule(AModule h) {
		if(handlers.containsKey(h.name)) {
			System.out.println("Handler already exists!");
		} else {
			handlers.put(h.name, h);
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
				for(AModule module : prov.getModules()) {
					registerModule(module);
				}
			} else {
				System.out.println("Module Provider \"" + prov.getProviderName() + "\" was disabled");
			}
		}
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent e) {
		registerProviders();
		registerModules();
		
		File configDir = null;
		
		try {
			File saves = FMLCommonHandler.instance().getSavesDirectory();
			File mcroot = null;
			
			if(FMLCommonHandler.instance().getSide() == Side.CLIENT) {
				mcroot = saves.getParentFile();
			} else {
				mcroot = saves;
			}
			
			configDir = new File(mcroot, "CelTweaker");
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		
		if(configDir == null) {
			return;
		}
		
		if(configDir.isFile()) {
			System.out.println("File at " + configDir.getAbsolutePath() + " is blocking the creation of config directory. Rename or delete it to continue.");
			return;
		}
		
		if(!configDir.isDirectory()) {
			if(!configDir.mkdirs()) {
				System.out.println("Error while creating config directory: " + configDir.getAbsolutePath());
				return;
			} else {
				try {
					BufferedWriter bw = new BufferedWriter(new FileWriter(new File(configDir, "sample.cfg")));
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
			}
		}
		
//		System.out.println("cfg dir: " + configDir.getAbsolutePath());
		
		File[] cfgFiles = configDir.listFiles(new FileFilter() {
			
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
				BufferedReader br = new BufferedReader(new FileReader(cfg));
				
				int ln = 1;
				String line = null;
				while((line = br.readLine()) != null) {
					if(!line.startsWith("#") && !isLineEmpty(line)) {
						//handle(line.split(".[^\\];"));
						try {
							handle(ModuleUtil.escapedSplit(line, ';'), cfg.getName(), ln);
						} catch(Exception eex) {
							System.out.println("Error parsing line " + ln + " in file: " + cfg.getName());
							eex.printStackTrace();
						}
					}
					
					ln++;
				}
				
				br.close();
			} catch(Exception ex) {
				System.out.println("Error while parsing file: " + cfg.getAbsolutePath());
				ex.printStackTrace();
			}
		}
	}
	
	private boolean isLineEmpty(String line) {
		for(int i = 0; i < line.length(); i++) {
			char c = line.charAt(i);
			if(c != ' ' && c != '\t') {
				return false;
			}
		}
		
		return true;
	}
	
	private void handle(String[] parts, String cfgName, int lineNumber) {
//		System.out.println("attHandle:");
//		Util.printStringArray(parts);
		if(parts.length > 0) {
			for(int i = 0; i < parts.length; i++) {
				parts[i] = parts[i].replace("\\", "");
			}
			
			AModule mod = handlers.get(parts[0]);
			if(mod != null) {
				String[] lArgs = new String[parts.length - 1];
				System.arraycopy(parts, 1, lArgs, 0, lArgs.length);
				
				Tweak tweak = mod.checkArgs(lArgs, cfgName, lineNumber);
				if(tweak != null) {
					if(mod.tweakApply(tweak)) {
						System.out.println("Successfully enabled tweak: " + tweak);
					} else {
						System.out.println("Failed to enable tweak: " + tweak);
					}
					
				}
			} else {
				System.out.println("Unknown handler module: " + parts[0]);
			}
		}
	}
}
