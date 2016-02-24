package com.crestech.opkey.plugin.communication.transport;

import java.util.concurrent.Semaphore;
import java.util.logging.Logger;

import javax.xml.bind.Marshaller;

import com.crestech.opkey.plugin.communication.contracts.functioncall.FunctionCall;
import com.crestech.opkey.plugin.communication.contracts.functioncall.FunctionCall.Function;
import com.crestech.opkey.plugin.communication.contracts.functionresult.FunctionResult;

public class BlockingInvocationChannel extends TransportLayer {

	private FunctionCall nextCall = null;
	private FunctionResult nextResult = null;
	private Logger logger = Logger.getLogger(this.getClass().getName());
	private Semaphore callLock = null;
	private Semaphore resultLock = null;

	public BlockingInvocationChannel() {
		callLock = new Semaphore(0);
		resultLock = new Semaphore(0);
	}

	public FunctionResult execute(FunctionCall fc) throws InterruptedException {
		nextCall = fc;
		callLock.release(); // allow the producer thread to inject the
							// functioncall inside the disptcher

		resultLock.acquire(); // wait while a result is generated

		FunctionResult res = nextResult;
		nextResult = null;
		return res;
	}

	public FunctionResult execute(String methodName, int timeoutInMillis) throws InterruptedException {
		FunctionCall fc = new FunctionCall();
		fc.setFunction(new Function());
		fc.getFunction().setCallTimeoutInMillis(timeoutInMillis);
		fc.getFunction().setMethodName(methodName);

		return execute(fc);
	}

	@Override
	public void run() {
		while (true) {
			try { // waiting for a function call
				callLock.acquire();

			} catch (InterruptedException e) {
				return; // thread killed. break out of operation
			}

			FunctionCall fc = nextCall;
			nextCall = null;

			if (fc != null)
				messageReceived(fc);

			else { // run ended
				logger.warning("null command received. Breaking out");
				resultLock.release();
				break;
			}
		}
	}

	@Override
	public void SendMessage(Object msg, Marshaller messageSerializer) throws Exception {

		// check if the outgoing message is indeed a function-result
		if (FunctionResult.class.isAssignableFrom(msg.getClass())) {

			FunctionResult res = (FunctionResult) msg;
			nextResult = res;

			resultLock.release(); // allow the consumer thread to get the result

		} else {
			logger.warning("Discarding " + msg.getClass().getName());
		}
	}

	@Override
	protected void setup() throws Exception {
		// NO-OP
	}

	@Override
	protected void cleanup() {
		nextCall = null;
		nextResult = null;

		callLock = null;
		resultLock = null;
	}

}
