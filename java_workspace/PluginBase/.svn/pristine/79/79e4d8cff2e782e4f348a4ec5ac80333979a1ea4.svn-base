package com.crestech.opkey.plugin.contexts;

public class Context {
	
	private static final ThreadLocal<InvocationContext> userThreadLocal = new ThreadLocal<InvocationContext>();
	private static final SessionContext sessionContext = new SessionContext();
	
	public static InvocationContext current() {
		return userThreadLocal.get();
	}
	
	public static SessionContext session() {
		return sessionContext;
	}
	
	public static void set(InvocationContext ctx) {
		userThreadLocal.set(ctx);
	}

	public static void clear() {
		userThreadLocal.remove();
	}
}
