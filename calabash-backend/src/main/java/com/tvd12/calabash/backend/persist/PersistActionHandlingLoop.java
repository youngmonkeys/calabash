package com.tvd12.calabash.backend.persist;

import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.tvd12.calabash.backend.manager.BytesMapPersistManager;
import com.tvd12.ezyfox.concurrent.EzyExecutors;
import com.tvd12.ezyfox.util.EzyLoggable;
import com.tvd12.ezyfox.util.EzyStartable;
import com.tvd12.ezyfox.util.EzyStoppable;

public class PersistActionHandlingLoop
		extends EzyLoggable 
		implements EzyStartable, EzyStoppable {

	protected final BytesMapPersistManager mapPersistManager;
	protected final PersistActionQueueManager actionQueueManager;
	protected final ScheduledExecutorService scheduledExecutorService;
	
	public PersistActionHandlingLoop(PersistActionQueueManager actionQueueManager) {
		this.actionQueueManager = actionQueueManager;
		this.scheduledExecutorService = newScheduledExecutorService();
	}
	
	protected ScheduledExecutorService newScheduledExecutorService() {
		ScheduledExecutorService service = EzyExecutors.newScheduledThreadPool(5, "calabash-persist-loop");
		Runtime.getRuntime().addShutdownHook(new Thread(() -> service.shutdown()));
		return service;
	}
	
	@Override
	public void start() throws Exception {
		this.scheduledExecutorService.scheduleAtFixedRate(this::handle, 100, 100, TimeUnit.MILLISECONDS);
	}
	
	protected void handle() {
		Map<String, PersistActionQueue> readyQueues = actionQueueManager.getReadyQueues();
		for(String mapName : readyQueues.keySet()) {
			PersistActionQueue queue = readyQueues.get(mapName);
			handleQueue(mapName, queue);
		}
	}
	
	protected void handleQueue(String mapName, PersistActionQueue queue) {
		
	}
	
	@Override
	public void stop() {
		this.scheduledExecutorService.shutdown();
	}
}
