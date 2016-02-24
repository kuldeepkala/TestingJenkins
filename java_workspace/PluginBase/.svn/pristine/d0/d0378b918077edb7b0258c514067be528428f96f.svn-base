package com.crestech.opkey.plugin.exceptionhandling;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.crestech.opkey.plugin.ExecutionStatus;
import com.crestech.opkey.plugin.ResultCodes;
import com.crestech.opkey.plugin.communication.contracts.functionresult.FunctionResult;

public class ExceptionTrap {

	private Logger logger = Logger.getLogger(this.getClass().getName());

	private ExceptionHandler2 exHandler = null;

	public ExceptionTrap(ExceptionHandler2 throwableHandler) {
		exHandler = throwableHandler;
	}

	public FunctionResult insideKeywordMethod(ExecutionException e, String methodName) {
		return handleExecutionException(e, methodName);
	}

	public FunctionResult beforeMethodCalled(Throwable e, String calledMethodName) {
		Class<?> eClass = e.getClass();
		
		if(MethodNotFoundException.class.isAssignableFrom(eClass))
			return getResultForMethodNotFound(e.getMessage());
		
		if(NoClassDefFoundError.class.isAssignableFrom(eClass))
			return getResultForClasNotFound(e);
		
		if(ClassNotFoundException.class.isAssignableFrom(eClass))
			return getResultForClasNotFound(e);
		
		logger.warning("Unknown exception while dispatching. " + e.toString());		
		return deployUserDefinedExceptionHandlers(e, calledMethodName);
	}

	public FunctionResult timeOut(String logMessage) {
		return getResultForTimeout(logMessage);
	}

	private FunctionResult handleExecutionException(Throwable executionException, String methodName) {
		// this happens when unhandled exception arises inside user library
		Throwable rootCause = getRootCause(executionException);

		// now attempt to handle the various scenarios

		if (rootCause instanceof MethodSignatureMismatchException) {
			// this happens when the method signature does not agree
			return getResultForSignatureMismatch(rootCause);

		} else {
			String msg = " caught. Attempting to handle.";
			logger.log(Level.FINE, rootCause.getClass().getName() + msg);
			return deployUserDefinedExceptionHandlers(rootCause, methodName);
		}
	}

	private Throwable getRootCause(Throwable executionException) {
		Throwable rootCause = null;

		// first obtain the root cause of exception

		if ((executionException.getCause() != null) && (executionException.getCause() instanceof java.lang.reflect.InvocationTargetException) && (executionException.getCause().getCause() != null)) {

			// must be some exception thrown by code inside user library
			rootCause = executionException.getCause().getCause();

		} else if (executionException.getCause() != null) {
			/*
			 * happens when some exception is thrown after the method invocation
			 * is started but before the method is actually invoked. there
			 * exists a narrow land of uncertainties there.
			 */
			rootCause = executionException.getCause();

		} else { // this never happens
			rootCause = executionException;
		}

		return rootCause;
	}

	private FunctionResult deployUserDefinedExceptionHandlers(Throwable e, String calledMethodName) {
		truncateStackTraceBelowCalledMethod(e, calledMethodName);

		if (exHandler.canHandle(e).getPonts() > 0) {
			return exHandler.handle(e);
		}

		/*
		 * if you have come this far then it mens that no proper exception
		 * handler was attached by the user. we provide a simple exception
		 * handler
		 */

		logger.log(Level.SEVERE, "None of the registered exception handlers were capable to handle an exception.", e);

		return getResultForunhandledException(e);
	}

	private void truncateStackTraceBelowCalledMethod(Throwable e, String calledMethodName) {
		StackTraceElement[] fullStack = e.getStackTrace();
		ArrayList<StackTraceElement> partStack_1 = new ArrayList<>();

		for (int i = 0; i < fullStack.length; i++) {
			StackTraceElement elm = fullStack[i];
			partStack_1.add(elm);

			if (elm.getMethodName().endsWith(calledMethodName))
				break;
		}

		StackTraceElement[] partStack_2;
		partStack_2 = partStack_1.toArray(new StackTraceElement[partStack_1.size()]);
		e.setStackTrace(partStack_2);
	}

	private FunctionResult getResultForTimeout(String logMessage) {
		FunctionResult fRes = new FunctionResult();
		fRes.setMessage(logMessage);
		fRes.setResultCode(ResultCodes.ERROR_TIMEOUT.Code());
		fRes.setStatus(ExecutionStatus.Fail.toString());
		return fRes;
	}

	private FunctionResult getResultForSignatureMismatch(Throwable e) {
		FunctionResult fRes = new FunctionResult();
		fRes.setMessage(e.getMessage());
		fRes.setResultCode(ResultCodes.ERROR_METHOD_SIGNATURE_MISMATCH.Code());
		fRes.setStatus(ExecutionStatus.Fail.toString());
		return fRes;
	}

	private FunctionResult getResultForunhandledException(Throwable e) {
		FunctionResult fRes = new FunctionResult();
		fRes.setMessage(getStackTrace(e));
		fRes.setResultCode(ResultCodes.ERROR_UNHANDLED_EXCEPTION.Code());
		fRes.setStatus(ExecutionStatus.Fail.toString());
		return fRes;
	}

	private FunctionResult getResultForMethodNotFound(String message) {
		FunctionResult immediateResult = new FunctionResult();

		immediateResult.setMessage(message);
		immediateResult.setOutput(null);
		immediateResult.setResultCode(ResultCodes.ERROR_METHOD_NOT_FOUND.Code());
		immediateResult.setStatus(ExecutionStatus.Fail.toString());

		return immediateResult;
	}

	private FunctionResult getResultForClasNotFound(Throwable e) {
		FunctionResult immediateResult = new FunctionResult();
		String message = getStackTrace(e);

		immediateResult.setMessage(message);
		immediateResult.setOutput(null);
		immediateResult.setResultCode(ResultCodes.ERROR_UNSATISFIED_DEPENDENCIES.Code());
		immediateResult.setStatus(ExecutionStatus.Fail.toString());

		return immediateResult;
	}

	private String getStackTrace(Throwable ex) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		ex.printStackTrace(pw);
		pw.close();
		return sw.toString();
	}
}
