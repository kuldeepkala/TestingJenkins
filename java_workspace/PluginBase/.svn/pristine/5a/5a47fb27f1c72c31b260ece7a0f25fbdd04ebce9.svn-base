package com.crestech.opkey.plugin.functiondispatch;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.crestech.opkey.plugin.FunctionResultValidator;
import com.crestech.opkey.plugin.ResultCodes;
import com.crestech.opkey.plugin.communication.MalformedXmlException;
import com.crestech.opkey.plugin.communication.contracts.functioncall.FunctionCall;
import com.crestech.opkey.plugin.communication.contracts.functionresult.FunctionResult;
import com.crestech.opkey.plugin.communication.message.FunctionCallChannel;
import com.crestech.opkey.plugin.communication.transport.ChannelClosedException;

public class FunctionDispatchLoop implements Runnable{

	private FunctionCallChannel fCallChnel;
	private Dispatcher dispatcher;
	private FunctionResultValidator rv;
	private Logger logger = Logger.getLogger(this.getClass().getName());

	public FunctionDispatchLoop(FunctionCallChannel fCallChannel, Dispatcher dispatcher, FunctionResultValidator validator) {
		this.fCallChnel = fCallChannel;
		this.dispatcher = dispatcher;
		this.rv = validator;

		if (rv == null)
			rv = new FunctionResultValidator();	
	}

	public void run() {
		logger.finer("Starting method dispatch loop");

		while (true) {
			try {

				Thread.sleep(1);				

				// process a function-call if one is available ----------
				if (fCallChnel.hasMessage())
					processFunctionCall();

			} catch (InterruptedException e) {
				return;

			} catch (ChannelClosedException e) {
				// there is no point running the message pump.
				// without the transport channel, no message will ever come

				StringWriter sw = new StringWriter();
				e.printStackTrace(new PrintWriter(sw));
				String exceptionAsString = sw.toString();
				logger.severe(exceptionAsString);
				return;

			} catch (Throwable e) {
				StringWriter sw = new StringWriter();
				e.printStackTrace(new PrintWriter(sw));
				String exceptionAsString = sw.toString();
				logger.severe(exceptionAsString);
			}
		}
	}

	private void processFunctionCall() throws ChannelClosedException, InterruptedException {
		FunctionCall fc;
		FunctionResult immediateResult, validatedResult;

		try { // to process one call at a time

			fc = fCallChnel.NextCall();

			logger.finest("Dispatching function call: " + fc.getFunction().getMethodName() + "   >>>>>>>>>>>>>>>>");
			immediateResult = dispatcher.Dispatch(fc);

			immediateResult.setStepNumber(fc.getStepNumber());
			// might need to validate the result
			validatedResult = rv.Validate(immediateResult);

			if (validatedResult.getResultCode() != ResultCodes.ERROR_TIMEOUT.Code()) {
				try { // to write back the result
					logger.fine("Obtained result: [Status:" + validatedResult.getStatus() + "] " + "[Output:" + validatedResult.getOutput() + "] " + "[Message:" + validatedResult.getMessage() + "] " + "[ResultCode: " + validatedResult.getResultCode()
							+ "]");

					fCallChnel.SendResult(validatedResult);

				} catch (Throwable e) { // problem while transmitting result
										// back to OpKey
					logger.log(Level.SEVERE, e.getMessage(), e);
				}

			} else { /*
					 * step timed out. no need to send the result. execution
					 * system will automatically generate a dummy result by
					 * itself.
					 */
				logger.warning(validatedResult.getMessage());
			}

		} catch (MalformedXmlException e) {
			logger.severe(e.getMessage());

		} catch (ChannelClosedException e) {
			throw e;
		}
	}
}
