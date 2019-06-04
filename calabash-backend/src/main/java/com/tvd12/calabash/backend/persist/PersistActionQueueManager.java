package com.tvd12.calabash.backend.persist;

import java.util.HashMap;
import java.util.Map;

public class PersistActionQueueManager {

	protected final Map<String, PersistActionQueue> queues;
	protected final PersistActionQueueFactory queueFactory;
	
	public PersistActionQueueManager(PersistActionQueueFactory queueFactory) {
		this.queues = new HashMap<>();
		this.queueFactory = queueFactory;
	}
	
	public PersistActionQueue getQueue(String mapName) {
		PersistActionQueue queue = queues.get(mapName);
		if(queue == null)
			queue = newQueue(mapName);
		return queue;
	}
	
	protected PersistActionQueue newQueue(String mapName) {
		synchronized (queues) {
			PersistActionQueue queue = queues.get(mapName);
			if(queue == null) {
				queue = queueFactory.newActionQueue(mapName);
				queues.put(mapName, queue);
			}
			return queue;
		}
	}
	
	public Map<String, PersistActionQueue> getReadyQueues() {
		Map<String, PersistActionQueue> readyQueues = new HashMap<>();
		synchronized (queues) {
			for(String mapName : queues.keySet()) {
				PersistActionQueue queue = queues.get(mapName);
				if(queue.isReady())
					readyQueues.put(mapName, queue);
			}
		}
		return readyQueues;
	}
	
}
