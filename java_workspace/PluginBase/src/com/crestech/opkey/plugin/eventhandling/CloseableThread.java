package com.crestech.opkey.plugin.eventhandling;

import java.io.Closeable;
import java.io.IOException;

public class CloseableThread extends Thread implements Closeable {

	public CloseableThread() {
	}

	public CloseableThread(Runnable target) {
		super(target);
		this.setName(target.getClass().getSimpleName());
		this.setDaemon(true);
	}

	public CloseableThread(String name) {
		super(name);
		this.setDaemon(true);
	}

	public CloseableThread(ThreadGroup group, Runnable target) {
		super(group, target);
		this.setName(target.getClass().getSimpleName());
		this.setDaemon(true);
	}

	public CloseableThread(ThreadGroup group, String name) {
		super(group, name);
		this.setDaemon(true);
	}

	public CloseableThread(Runnable target, String name) {
		super(target, name);
		this.setDaemon(true);
	}

	public CloseableThread(ThreadGroup group, Runnable target, String name) {
		super(group, target, name);
		this.setDaemon(true);
	}

	public CloseableThread(ThreadGroup group, Runnable target, String name, long stackSize) {
		super(group, target, name, stackSize);
		this.setDaemon(true);
	}

	@Override
	public void close() throws IOException {
		this.interrupt();

		try {
			this.join();
		} catch (InterruptedException e) {
			return;
		}
	}
}
