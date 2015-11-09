package celestibytes.celtweaker.api;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class ScriptReader {
	
	private final FileReader fr;
	private final String scriptFilename;
	
	private int lastChar = -1;
	private int currentLine = 1;
	
	public ScriptReader(File file) throws FileNotFoundException {
		fr = new FileReader(file);
		scriptFilename = file.getName();
	}
	
	public int getCurrentLine() {
		return currentLine;
	}
	
	public String getFilename() {
		return scriptFilename;
	}
	
	public int read() throws IOException {
		int c = fr.read();
		if(c == '\n') {
			if(lastChar == '\r') {
				lastChar = -1;
			} else {
				currentLine++;
				lastChar = c;
			}
		} else if(c == '\r') {
			if(lastChar == '\n') {
				lastChar = -1;
			} else {
				currentLine++;
				lastChar = c;
			}
		}
		
		return c;
	}
	
	public void close() throws IOException {
		fr.close();
	}
}
