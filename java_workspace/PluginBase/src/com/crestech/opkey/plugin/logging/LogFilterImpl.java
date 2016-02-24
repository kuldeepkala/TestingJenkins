package com.crestech.opkey.plugin.logging;

import java.util.logging.Filter;
import java.util.logging.Level;
import java.util.logging.LogRecord;

public class LogFilterImpl implements Filter {

	private String configratorPackageName = "";
	private boolean isDebugMode = false;

	public LogFilterImpl(String configratorPackageName, boolean isDebugMode) {
		this.configratorPackageName = configratorPackageName;
		this.isDebugMode = isDebugMode;
	}

	@Override
	public boolean isLoggable(LogRecord record) {
		if (isDebugMode) {
			return true;
			
		} else if(record.getLevel().intValue() < Level.INFO.intValue()) {
			//no dubug? discard everything below INFO
			return false;
		}

		String source = record.getSourceClassName();
		if (source == null)
			source = record.getLoggerName();

		// restrict logging to only from the classes in
		// the (sub)packages of the startup Main class.
		boolean shouldLog = source.startsWith(configratorPackageName) || source.startsWith("com.crestech");
		
		return shouldLog;
	}

}
