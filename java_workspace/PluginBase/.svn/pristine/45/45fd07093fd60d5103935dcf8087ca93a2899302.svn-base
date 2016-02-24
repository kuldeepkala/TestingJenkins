package com.crestech.opkey.plugin.exceptionhandling;

public class Comparetor {
	
	private Handleability handleability;
	private Operator operator;

	Comparetor(Handleability handleability, Operator operator) {
		this.handleability = handleability;
		this.operator = operator;
	}

	public ThrowableAdjective throwable(Throwable e) {
		return new ThrowableAdjective(e, handleability, operator);
	}
	
	public MessageAdjective messageOf(Throwable e) {
		return new MessageAdjective(e, handleability, operator);
	}
}