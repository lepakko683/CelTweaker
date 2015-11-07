package celestibytes.celtweaker;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;

import celestibytes.celtweaker.api.ILogger;

public class Logger implements ILogger {
	
	private final org.apache.logging.log4j.Logger rlog;
	
	public Logger(String logname) {
		rlog = LogManager.getLogger(logname);
	}
	
	private void log(Level lvl, Object txt) {
		rlog.log(lvl, txt.toString());
	}

	@Override
	public void err(Object txt) {
		log(Level.ERROR, txt);
	}

	@Override
	public void warn(Object txt) {
		log(Level.WARN, txt);
	}

	@Override
	public void info(Object txt) {
		log(Level.INFO, txt);
	}

	@Override
	public void debug(Object txt) {
		log(Level.DEBUG, txt);
	}

}
