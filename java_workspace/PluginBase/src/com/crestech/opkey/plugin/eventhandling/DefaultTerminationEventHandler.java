package com.crestech.opkey.plugin.eventhandling;

import java.io.Closeable;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DefaultTerminationEventHandler implements EventHandler {

	private Closeable[] closeableResources;
	private Logger logger = Logger.getLogger(this.getClass().getName());
	private Semaphore terminationSemaphore;

	public DefaultTerminationEventHandler(Closeable... resources) {
		terminationSemaphore = new Semaphore(0);
		closeableResources = resources;
	}

	@Override
	public UUID getEventID() {
		return UUID.fromString("ce333217-c425-4e04-9889-6bb01b5c05cd");
	}

	@Override
	public UUID getPublisherID() {
		return UUID.fromString("4f791206-7567-45cf-a982-c380ad225955");
	}

	@Override
	public String getEventName() {
		return "SESSION_ENDING";
	}

	@Override
	public Map<UUID, String> getFilterCriteria() {
		return null;
	}

	@Override
	public void onEventOccur(Map<UUID, String> filterCriteria) {
		try { // -----------------------
			beforeCleanup();
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
		}

		try { // -----------------------
			doCleanup();
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
		}

		try { // -----------------------
			afterCleanup();
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
		}

		try { // -----------------------
			Thread.sleep(4000); // allow signaled threads to die down
		} catch (InterruptedException e) {
		}

		this.terminationSemaphore.release();
	}

	private void doCleanup() {
		if (closeableResources == null)
			closeableResources = new Closeable[0];

		for (Closeable res : closeableResources) {
			if(res == null) continue;
			
			try {
				logger.finer("Closing " + res.toString());
				res.close();

			} catch (IOException e) {
				StringWriter sw = new StringWriter();
				e.printStackTrace(new PrintWriter(sw));
				String exceptionAsString = sw.toString();
				logger.warning(exceptionAsString);

			} catch (Exception e) {
				if (InterruptedException.class.isAssignableFrom(e.getClass()))
					continue;

				else
					e.printStackTrace();
			}
		}
	}

	public void waitForNextEvent() throws InterruptedException {
		terminationSemaphore.acquire();
	}

	protected void beforeCleanup() throws Exception {
		// override in derived class
	}

	protected void afterCleanup() throws Exception {
		// override in derived class
	}

}
