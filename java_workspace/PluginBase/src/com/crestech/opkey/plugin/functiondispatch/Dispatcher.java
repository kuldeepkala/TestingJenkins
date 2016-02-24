package com.crestech.opkey.plugin.functiondispatch;

import com.crestech.opkey.plugin.communication.contracts.functioncall.FunctionCall;
import com.crestech.opkey.plugin.communication.contracts.functionresult.FunctionResult;

public interface Dispatcher {
	
	FunctionResult Dispatch(FunctionCall fc) throws InterruptedException;
	
}
