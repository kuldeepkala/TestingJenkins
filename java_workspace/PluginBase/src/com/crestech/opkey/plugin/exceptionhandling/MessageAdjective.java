package com.crestech.opkey.plugin.exceptionhandling;

public class MessageAdjective {
	
	private Throwable throwable;
	private Handleability handleability;
	private Operator operator;
	
	MessageAdjective(Throwable e, Handleability handleability, Operator operator) {
		this.throwable = e;
		this.handleability = handleability;
		this.operator = operator;
	}

	public Handleability contains(String string) {
		int points = calculatePoints(throwable, string);
		handleability.push(operator, points);
		return handleability;
	}
	
	private int calculatePoints(Throwable e, String s) {
		int points = 0;
		
		if(s != null && s.length() > 0 && e.getMessage() != null && e.getMessage().contains(s)) {
			points = 100 + s.length();
		}
		
		return points;
	}
}