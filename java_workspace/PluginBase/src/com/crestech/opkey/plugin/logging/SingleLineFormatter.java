package com.crestech.opkey.plugin.logging;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.MessageFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;

import com.crestech.opkey.plugin.contexts.Context;

/*
 * http://stackoverflow.com/questions/194765/how-do-i-get-java-logging-output-to-
 * appear-on-a-single-line
 */
class SingleLineFormatter extends Formatter {

	Date dat = new Date();
	private final static String format = "{0, date, yyyy-MMM-dd HH:mm:ss.SSS}";
	private MessageFormat formatter = new MessageFormat(format);
	private Object args[] = new Object[1];

	private String lineSeparator = "\r\n";

	/**
	 * Format the given LogRecord.
	 * 
	 * @param record
	 *            the log record to be formatted.
	 * @return a formatted log record
	 */
	public synchronized String format(LogRecord record) {

		StringBuilder sb = new StringBuilder();

		// Minimize memory allocations here.
		dat.setTime(record.getMillis());
		args[0] = dat;

		// Date and time--------------------------
		StringBuffer buff = new StringBuffer();
		formatter.format(args, buff, null);
		sb.append(buff);
		sb.append(" ");

		String message = formatMessage(record);

		// log Level--------------------------------
		String level = record.getLevel().getLocalizedName();

		if (record.getLevel() == Level.SEVERE)
			level = "SEVERE";

		else if (record.getLevel() == Level.WARNING)
			level = "WARNING";

		else if (record.getLevel() == Level.INFO)
			level = "INFO";

		else if (record.getLevel() == Level.FINE)
			level = "VERBOSE";

		else if (record.getLevel() == Level.FINER)
			level = "TRACE";

		else if (record.getLevel() == Level.FINEST)
			level = "TRACE";

		else
			level = "TRACE";

		sb.append(String.format("%-10s", "(" + level + ")"));

		// log-source for debug runs
		if (Context.session().isDebugMode()) {
			String loggerName = record.getLoggerName();
			String[] loggerNameParts = loggerName.split("\\.");

			for(String logSourcePart : loggerNameParts) {
				sb.append("[");
				sb.append(logSourcePart);
				sb.append("]");
			}
			
			sb.append("[");
			sb.append(record.getSourceMethodName());
			sb.append("] ");
		}

		// actual message------------------------
		sb.append(message);

		// lineSeparator-------------------------
		sb.append(lineSeparator);

		// exception if any-----------------------
		if (record.getThrown() != null) {
			try (StringWriter sw = new StringWriter(); PrintWriter pw = new PrintWriter(sw);) {
				record.getThrown().printStackTrace(pw);
				sb.append(sw.toString());

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		// done----------------------------------
		return sb.toString();
	}
}