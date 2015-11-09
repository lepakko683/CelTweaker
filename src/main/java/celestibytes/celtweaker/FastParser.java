package celestibytes.celtweaker;

import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.minecraftforge.oredict.OreDictionary;
import celestibytes.celtweaker.api.AModule.Tuple;
import celestibytes.celtweaker.api.ICfgParser;
import celestibytes.celtweaker.api.ILogger;
import celestibytes.celtweaker.api.ITweakHandler;
import celestibytes.celtweaker.api.ScriptReader;
import celestibytes.celtweaker.api.Tweak;
import celestibytes.celtweaker.api.types.TBase;
import celestibytes.celtweaker.api.types.TBlock;
import celestibytes.celtweaker.api.types.TChar;
import celestibytes.celtweaker.api.types.TClass;
import celestibytes.celtweaker.api.types.TItem;
import celestibytes.celtweaker.api.types.TNumber;
import celestibytes.celtweaker.api.types.TOre;
import celestibytes.celtweaker.api.types.TStack;
import celestibytes.celtweaker.api.types.TString;

public class FastParser implements ICfgParser {
	
	private static final Map<String, Action> actions = new HashMap<String, Action>();
	
	private static void addAction(Action a) {
		actions.put(a.name, a);
	}
	
	static {
		addAction(new Action("Block") {
			
			@Override
			public TBase run(TBase[] args) {
				if(args.length != 2) {
					return null;
				}
				
				return new TBlock(args[1].asString());
			}
		});
		addAction(new Action("Item") {
			
			@Override
			public TBase run(TBase[] args) {
				if(args.length != 2) {
					return null;
				}
				
				return new TItem(args[1].asString());
			}
		});
		addAction(new Action("Stack") {
			
			@Override
			public TBase run(TBase[] args) {
				if(args.length <= 1) {
					return null;
				}
				
				TBase[] aa = args; // TODO: cleanup
				
				TNumber tcount = aa.length >= 3 ? aa[2].castNumber() : null;
				TNumber tmetadata = null;
				if(aa.length >= 4) {
					if(aa[3] instanceof TString) {
						tmetadata = new TNumber(aa[3].castString().asString().equals("*") ? OreDictionary.WILDCARD_VALUE : 0);
					} else if(aa[3] instanceof TNumber) {
						tmetadata = aa[3].castNumber();
					}
				}
				
				int count = tcount == null ? 1 : tcount.asInt();
				int metadata = tmetadata == null ? 0 : tmetadata.asInt();
				
				TItem item;
				
				if(aa[1] instanceof TString) {
					item = new TItem(aa[1].castString().asString());
				} else {
					item = aa[1].castItem();
				}
				
				if(item == null) {
//					TBlock block = aa[1].castBlock();
//					if(block != null) {
//						return new TStack(block, count, metadata);
//					}
				} else {
					return new TStack(item, count, metadata);
				}
				
				return null;
			}
		});
		addAction(new Action("Ore") {
			
			@Override
			public TBase run(TBase[] args) {
				if(args.length != 2) {
					return null;
				}
				
				return new TOre(args[1].asString()); 
			}
		});
		addAction(new Action("Class") {

			@Override
			public TBase run(TBase[] args) {
				if(args.length != 2) {
					return null;
				}
				
				return new TClass(args[1].asString());
			}
			
		});
	}
	
	private static final String whitespace = " \t\n\r#";

	@Override
	public List<Tweak> parseTweaks(ScriptReader r, ITweakHandler th, ILogger log) throws IOException {
		List<Tweak> ret = new LinkedList<Tweak>();
		
		int c = -1;
		while((c = r.read()) != -1) {
			if(c == '#') {
				skipComment(r);
			} else if(c == '(') {
				int lineNumber = r.getCurrentLine();
				ret.add(th.handleTweak(parseRoot(r), log, lineNumber, r.getFilename()));
			}
		}
		
		return ret;
	}
	
	private List<TBase> parseRoot(ScriptReader r) throws IOException {
		List<TBase> ret = new LinkedList<TBase>();
		
		boolean ws = true;
		int c = -1;
		while((c = r.read()) != -1) {
			if(ws) {
				if(c == '#') {
					skipComment(r);
				} else if(whitespace.indexOf(c) == -1) {
					ws = false;
				}
			}
			
			if(!ws) {
				if(c == ')') {
					return ret;
				} else if(c == '(') {
					ret.add(parseAction(r));
					ws = true;
				} else if(c == '"' || c == '\'') {
					ret.add(parseText(r, (char) c));
					ws = true;
				} else if(whitespace.indexOf(c) == -1) {
					Tuple<TBase, Character> uq = parseUnquoted(r, (char) c);
					ret.add(uq.a);
					if(uq.b == ')') {
						return ret;
					}
					
					ws = true;
				}
			}
		}
		
		return null;
	}
	
	private TBase parseAction(ScriptReader r) throws IOException {
		List<TBase> ret = new LinkedList<TBase>();
		
		boolean ws = true;
		int c = -1;
		while((c = r.read()) != -1) {
			if(ws) {
				if(c == '#') {
					skipComment(r);
				} else if(whitespace.indexOf(c) == -1) {
					ws = false;
				}
			}
			
			if(!ws) {
				if(c == ')') {
					return doAction(ret);
				} else if(c == '(') {
					ret.add(parseAction(r));
					ws = true;
				} else if(c == '"' || c == '\'') {
					ret.add(parseText(r, (char) c));
					ws = true;
				} else if(whitespace.indexOf(c) == -1) {
					Tuple<TBase, Character> uq = parseUnquoted(r, (char) c);
					ret.add(uq.a);
					if(uq.b == ')') {
						return doAction(ret);
					}
					
					ws = true;
				}
			}
		}
		
		return null;
	}
	
	private static final String unquotedEnd = whitespace + ")";
	
	private Tuple<TBase, Character> parseUnquoted(ScriptReader r, char cc) throws IOException {
		StringBuilder sb = new StringBuilder(Character.toString(cc));
		
		int c = -1;
		while((c = r.read()) != -1) {
			if(unquotedEnd.indexOf(c) != -1) {
				return new Tuple<TBase, Character>(TBase.parseType(sb.toString()), (char) c);
			} else {
				sb.append((char) c);
			}
		}
		
		return null;
	}
	
	private TBase parseText(ScriptReader r, char quote) throws IOException {
		StringBuilder sb = new StringBuilder();
		
		int c = -1;
		int lastc = -1;
		while((c = r.read()) != -1) {
			if(c == quote && lastc != '\\') {
				if(quote == '"') {
					return new TString(sb.toString());
				} else if(quote == '\'') {
					return new TChar(sb.toString());
				}
			}
			
			sb.append((char) c);
			lastc = c;
		}
		
		return null;
	}
	
	private void skipComment(ScriptReader r) throws IOException {
		int c = -1;
		while((c = r.read()) != -1) {
			if(c == '\n' || c == '\r') {
				return;
			}
		}
	}
	
	private TBase doAction(List<TBase> list) {
//		for(TBase i : list) {
//			System.out.println("--->" + i);
//		}
		
		if(list.size() >= 1) {
			TBase[] args = list.toArray(new TBase[0]);
			Action a = actions.get(args[0].asString());
			
			if(a != null) {
				return a.run(args);
			} else {
				System.out.println("Unknown action!");
			}
		}
		
		return null;
	}
	
	private static abstract class Action {
		public final String name;
		
		public Action(String name) {
			this.name = name;
		}
		
		public abstract TBase run(TBase[] args);
	}
	
}
