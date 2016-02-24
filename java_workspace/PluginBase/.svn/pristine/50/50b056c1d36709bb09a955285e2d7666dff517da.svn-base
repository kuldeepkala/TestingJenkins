package com.crestech.opkey.plugin.exceptionhandling;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.logging.Logger;

import com.crestech.opkey.plugin.communication.contracts.functionresult.FunctionResult;
import com.crestech.opkey.plugin.functiondispatch.ExceptionHandler;
import com.crestech.opkey.plugin.functiondispatch.Tuple;

public class HandlerCollection implements ExceptionHandler2 {

	private Logger logger = Logger.getLogger(this.getClass().getName());
	private List<ExceptionHandler2> _exhandlers;

	public HandlerCollection(List<ExceptionHandler2> exHandlers) {
		_exhandlers = exHandlers;
	}

	public HandlerCollection(ExceptionHandler[] oldExceptionHandlers) {
		_exhandlers = new ArrayList<ExceptionHandler2>();
		for (ExceptionHandler exh : oldExceptionHandlers) {
			_exhandlers.add(new HandlerAdapter(exh));
		}
	}

	public HandlerCollection(ExceptionHandler[] oldExHandlers, List<ExceptionHandler2> exHandlers) {
		this(oldExHandlers);
		_exhandlers.addAll(exHandlers);
	}

	@Override
	public Handleability canHandle(Throwable e) {
		Tuple<ExceptionHandler2, Handleability> th = getHandler(e);
		if (th == null) {
			return new Handleability(0);
		} else {
			return th.y;
		}
	}

	private Tuple<ExceptionHandler2, Handleability> getHandler(Throwable e) {
		// find the highest handleability
		SortedMap<Handleability, ExceptionHandler2> dict = new TreeMap<>();

		for (ExceptionHandler2 th : _exhandlers) {
			Handleability h = th.canHandle(e);
			if (h.getPonts() > 0) {
				dict.put(h, th);
			}
		}

		if (dict.size() > 0) {
			Handleability h = dict.lastKey();
			ExceptionHandler2 th = dict.get(h);
			log(dict, e, th);

			return new Tuple<ExceptionHandler2, Handleability>(th, h);

		} else {
			return null;
		}
	}

	private Tuple<Long, Throwable> lastLogged = null;

	private void log(SortedMap<Handleability, ExceptionHandler2> dict, Throwable e, ExceptionHandler2 th) {
		if (lastLogged != null && lastLogged.y == e && lastLogged.x > (System.currentTimeMillis() - 1000))
			return; // already logged less than 1sec. ago
		
		lastLogged = new Tuple<Long, Throwable>(System.currentTimeMillis(), e);

		StringBuilder sb = new StringBuilder();
		for (Handleability hh : dict.keySet()) {
			ExceptionHandler2 thh = dict.get(hh);
			sb.append(getHandlerClassName(thh) + "(" + hh.getPonts() + "), ");
		}

		logger.finest(dict.size() + "/" + _exhandlers.size()  + " handlers accept " + e.getClass().getSimpleName() + ". Best being " + getHandlerClassName(th));
		logger.finest(sb.toString());
	}

	@Override
	public FunctionResult handle(Throwable e) {
		Tuple<ExceptionHandler2, Handleability> th = getHandler(e);
		logger.fine("Attempting to handle " + e.getClass().getSimpleName() + " using " + getHandlerClassName(th.x));
		return th.x.handle(e);
	}

	private String getHandlerClassName(Object th) {
		if (HandlerAdapter.class.isAssignableFrom(th.getClass())) {
			return getHandlerClassName(((HandlerAdapter) th).getInnerHandler());

		} else {
			return th.getClass().getSimpleName();
		}
	}

}
