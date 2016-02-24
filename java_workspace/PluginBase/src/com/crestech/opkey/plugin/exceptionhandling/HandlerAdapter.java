package com.crestech.opkey.plugin.exceptionhandling;

import com.crestech.opkey.plugin.communication.contracts.functionresult.FunctionResult;
import com.crestech.opkey.plugin.functiondispatch.ExceptionHandler;

public class HandlerAdapter implements ExceptionHandler2 {
	
	private final ExceptionHandler _exHandler;
	
	public HandlerAdapter(ExceptionHandler exHandler) {
		_exHandler = exHandler;
	}

	@Override
	public Handleability canHandle(Throwable e) {
		return CanHandle.givenThat().throwable(e).isSubclassOf(_exHandler.getExceptionType());
	}

	@Override
	public FunctionResult handle(Throwable e) {
		return _exHandler.handle(e);
	}

	public ExceptionHandler getInnerHandler() {
		return _exHandler;
	}

}
