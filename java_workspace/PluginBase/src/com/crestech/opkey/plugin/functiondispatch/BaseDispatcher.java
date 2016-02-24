package com.crestech.opkey.plugin.functiondispatch;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.crestech.opkey.plugin.KeywordLibrary;
import com.crestech.opkey.plugin.communication.contracts.functioncall.FunctionCall;
import com.crestech.opkey.plugin.communication.contracts.functionresult.FunctionResult;
import com.crestech.opkey.plugin.contexts.InvocationContext;
import com.crestech.opkey.plugin.exceptionhandling.HandlerCollection;
import com.crestech.opkey.plugin.exceptionhandling.ExceptionTrap;
import com.crestech.opkey.plugin.exceptionhandling.MethodNotFoundException;
import com.crestech.opkey.plugin.exceptionhandling.ExceptionHandler2;

public class BaseDispatcher implements Dispatcher {

	private Logger logger = Logger.getLogger(this.getClass().getName());

	protected LibraryLocator locator = null;
	protected ArgumentFormatter argFormatter = null;
	protected ExceptionTrap exceptionTrap = null;

	protected Map<String, Method> knownMethods = new HashMap<String, Method>();
	protected Map<Method, KeywordLibrary> knownClasses = new HashMap<Method, KeywordLibrary>();

	private BaseDispatcher(LibraryLocator locator, ArgumentFormatter argFormatter) {
		this.locator = locator;
		this.argFormatter = argFormatter;
	}

	public BaseDispatcher(LibraryLocator locator, ArgumentFormatter argFormatter, List<ExceptionHandler2> newExceptionHandlers) {
		this(locator, argFormatter);
		this.exceptionTrap = new ExceptionTrap(new HandlerCollection(newExceptionHandlers));
	}

	public BaseDispatcher(LibraryLocator locator, ArgumentFormatter argFormatter, ExceptionHandler[] oldExceptionHandlers) {
		this(locator, argFormatter);
		this.exceptionTrap = new ExceptionTrap(new HandlerCollection(oldExceptionHandlers));
	}

	public BaseDispatcher(LibraryLocator locator, ArgumentFormatter argFormatter, ExceptionHandler[] oldExceptionHandlers, List<ExceptionHandler2> newExceptionHandlers) {
		this(locator, argFormatter);
		this.exceptionTrap = new ExceptionTrap(new HandlerCollection(oldExceptionHandlers, newExceptionHandlers));
	}

	@Override
	public FunctionResult Dispatch(FunctionCall fc) throws InterruptedException {
		try {
			return Dispatch_1(fc);

		} catch (InterruptedException e) {
			throw e;

		} catch (Throwable e) {
			// something bad happened before the function is actually invoked
			return exceptionTrap.beforeMethodCalled(e, fc.getFunction().getMethodName());
		}
	}

	private FunctionResult Dispatch_1(FunctionCall fc) throws Exception {
		long timeoutInMillis = fc.getFunction().getCallTimeoutInMillis();		
		String methodName = fc.getFunction().getMethodName();
		
		Method methd = locateMethod(methodName);
		
		Object methodContainer = knownClasses.get(methd);
		Object[] args = argFormatter.Format(fc);
		
		// generating the method signature.
		// will use this for logging purpose
		String methodSignature = CallableMethod.generateSignature(args, methd.getName());
		
		// invocation context : this is different for each method call.
		InvocationContext ctx = new InvocationContext(fc);
		
		// create new task
		FutureTask<FunctionResult> theTask = new ExecutionTask<FunctionResult>(new CallableMethod(methd, methodContainer, args, ctx));

		/*
		 * Here is the complete call hierarchy
		 * 
		 * Dispatcher \ Thread \ FutureTask \ CallableMethod \ Actual Method
		 */

		// start task in a new thread
		logger.fine("Invoking " + methodSignature);

		try { // to wait until method finishes
			FunctionResult fRes = theTask.get(timeoutInMillis, TimeUnit.MILLISECONDS);
			return fRes;

		} catch (InterruptedException e) {
			// just signal cancellation. do not wait for it, neither guarantee
			logger.finer(this.getClass().getSimpleName() + " is abandoning " + methodName + ". It's behavior would get unpredictable");
			theTask.cancel(true);
			throw e; // bubble up

		} catch (ExecutionException e) {
			// exception inside user library
			return exceptionTrap.insideKeywordMethod(e, methodName);

		} catch (TimeoutException e) {
			// happens when function-call times out
			String logMessage = "Timeout (" + timeoutInMillis + "ms) exceedeed while executing '" + methodName + "'.";
			logger.log(Level.WARNING, logMessage);
			return exceptionTrap.timeOut(logMessage);
		}
	}

	protected Method locateMethod(String methodName) throws MethodNotFoundException {
		if (knownMethods.keySet().contains(methodName)) {
			return knownMethods.get(methodName);

		} else {
			Tuple<Method, KeywordLibrary> ref = locator.Locate(methodName);
			if (ref == null) {
				throw new MethodNotFoundException(methodName);

			} else {
				Method methd = ref.x;
				knownClasses.put(methd, ref.y);
				knownMethods.put(methodName, methd);
				return methd;
			}
		}
	}
}
