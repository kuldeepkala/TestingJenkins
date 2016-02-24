package com.crestech.opkey.plugin.contexts;

import java.util.logging.Logger;

import com.crestech.opkey.plugin.communication.contracts.functioncall.FunctionCall;

public class InvocationContext {

	private FunctionCall functioncall;
	private long startTime;

	public InvocationContext(FunctionCall functionCall) {
		this.functioncall = functionCall;
		startTime = System.nanoTime();
	}

	public FunctionCall getFunctionCall() {
		return functioncall;
	}

	public int getCallTimeoutInMillis() {
		return functioncall.getFunction().getCallTimeoutInMillis();
	}

	public int getCallRemainingMillis() {
		return this.getCallTimeoutInMillis() - this.getCallElapsedMillis();
	}

	public int getCallElapsedMillis() {
		return (int) ((System.nanoTime() - startTime) / 1000000l);
	}

	
	@Deprecated
	public Logger getLogger() {
		String callerClassName = new Throwable().getStackTrace()[0].getClassName();
		Logger logger = Logger.getLogger(callerClassName);
		return logger;
	}
}