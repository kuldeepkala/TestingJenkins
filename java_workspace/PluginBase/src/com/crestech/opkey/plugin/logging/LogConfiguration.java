package com.crestech.opkey.plugin.logging;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.crestech.opkey.plugin.contexts.Context;

//SEE: https://docs.oracle.com/javase/7/docs/api/java/util/logging/LogManager.html

public final class LogConfiguration {

	private static List<Logger> ignoredLoggers = new ArrayList<>();
	private static List<String> ignoredPackages = new ArrayList<>();

	static {
		ignoredPackages.add("javax.xml.bind");
		ignoredPackages.add("com.sun.xml");		
	}
	
	private LogConfiguration() {
		/* just to prevent instantiation */
	}

	public static void configure() throws Exception {
		if (System.getProperty("java.util.logging.config.class") != null)
			return;

		if (System.getProperty("java.util.logging.config.file") != null)
			return;

		Level ALL = Level.ALL;
		Logger rootLogger = Logger.getLogger("");
		rootLogger.setLevel(ALL);

		//set console to use utf-8
		System.setOut(new PrintStream(System.out, true, "utf-8"));
		System.setErr(new PrintStream(System.err, true, "utf-8"));
		
		String callerPackageName_1 = getCallerPackage();
		System.out.println(">> Caller package name is " + callerPackageName_1);
		
		boolean isDebugMode = Context.session().isDebugMode();

		for (Handler handler : rootLogger.getHandlers()) {
			if (handler instanceof ConsoleHandler) {
				
				handler.setLevel(ALL);
				handler.setEncoding("utf-8");				
				handler.setFormatter(new SingleLineFormatter());
				handler.setFilter(new LogFilterImpl(callerPackageName_1, isDebugMode));
			}
		}

		// the following loggers are suppressed
		
		for (String packageName : ignoredPackages) {
			Logger logger = Logger.getLogger(packageName);
			logger.setLevel(Level.WARNING);
			ignoredLoggers.add(logger);
		}
	}
	
	private static String getCallerPackage() {
		int idx = 2; //0=me, 1=my-caller, 2=my-caller's-caller
		String callerPackageName = "";
		
		StackTraceElement caller = new Throwable().getStackTrace()[idx];
		
		String callerFullName = caller.getClassName();
		
		int lastPeriodAt = callerFullName.lastIndexOf(".");		
		
		if (lastPeriodAt > 0) {
			callerPackageName = callerFullName.substring(0, lastPeriodAt);
		}
		
		return callerPackageName;
	}
}