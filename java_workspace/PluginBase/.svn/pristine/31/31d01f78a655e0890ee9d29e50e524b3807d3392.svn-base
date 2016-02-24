package com.crestech.opkey.plugin.functiondispatch;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

class ExecutionTask<V> extends FutureTask<V> {

	public ExecutionTask(Callable<V> callable) {
		super(callable);
	}

	private void preGet() {
		Thread t = new Thread(this);
		t.setDaemon(true);
		t.start();
	}

	@Override
	public V get() throws ExecutionException, InterruptedException {
		preGet();
		return super.get();
	}

	@Override
	public V get(long timeout, TimeUnit unit) throws ExecutionException, InterruptedException, TimeoutException {
		preGet();
		return super.get(timeout, unit);
	}

}
