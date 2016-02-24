package com.crestech.opkey.plugin.functiondispatch;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;

import com.crestech.opkey.plugin.communication.contracts.functionresult.FunctionResult;
import com.crestech.opkey.plugin.contexts.Context;
import com.crestech.opkey.plugin.contexts.InvocationContext;
import com.crestech.opkey.plugin.exceptionhandling.MethodSignatureMismatchException;

public class CallableMethod implements Callable<FunctionResult> {

	private Method m;
	Object obj;
	Object[] args;
	InvocationContext context;

	public CallableMethod(Method m, Object obj, Object[] args, InvocationContext context) {
		this.m = m;
		this.obj = obj;
		this.args = args;
		this.context = context;
	}

	@Override
	public FunctionResult call() throws Exception {
		try {
			Context.set(context);
			Thread.currentThread().setName(m.getName());
			return (FunctionResult) m.invoke(obj, args);

		} catch (IllegalArgumentException e) {
			String calledSignature = generateSignature(args, context.getFunctionCall().getFunction().getMethodName());
			String message = "Method signature mismatch. Called: '" + calledSignature + "', Implemented: '" + generateSignature(m) + "'";
			throw new MethodSignatureMismatchException(message, e);
			
		} finally {
			Context.clear();
		}
	}

	public static String generateSignature(Object[] args, String methodName) {
		String methodSignature = "";

		for (Object arg : args)
			methodSignature = methodSignature + arg.getClass().getSimpleName() + ", ";

		if (methodSignature.length() > 2)
			methodSignature = methodSignature.substring(0, methodSignature.length() - 2);

		methodSignature = methodName + "(" + methodSignature + ")";
		return methodSignature;
	}

	public static String generateSignature(Method method) {
		String methodSignature = "";

		for (Class<?> cls : method.getParameterTypes())
			methodSignature = methodSignature + cls.getSimpleName() + ", ";

		if (methodSignature.length() > 2)
			methodSignature = methodSignature.substring(0, methodSignature.length() - 2);

		methodSignature = method.getName() + "(" + methodSignature + ")";
		return methodSignature;
	}
}
