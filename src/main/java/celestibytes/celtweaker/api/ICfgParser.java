package celestibytes.celtweaker.api;

import java.io.IOException;
import java.io.Reader;
import java.util.List;


public interface ICfgParser {
	
	public List<Tweak> parseTweaks(Reader r, ITweakHandler th, ILogger log) throws IOException;
	
}
