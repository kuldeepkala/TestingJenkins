package com.crestech.opkey.plugin.eventhandling;

import java.util.Map;
import java.util.UUID;

public interface EventHandler {
	
	public UUID getEventID() ;
	
	public UUID getPublisherID();
	
	public String getEventName();
	
	public Map<UUID, String> getFilterCriteria();
	
	public void onEventOccur(Map<UUID, String> filterCriteria);

	public void waitForNextEvent() throws InterruptedException;

}
