package com.tvd12.calabash.local.persist;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class PersistActionBulkTicketQueues {

	protected final Map<String, PersistActionBulkQueue> queues;
	protected final BlockingQueue<PersistActionBulkQueue> ticketQueue;
	
	public PersistActionBulkTicketQueues() {
		this.queues = new HashMap<>();
		this.ticketQueue = new LinkedBlockingQueue<>();
	}
	
	public PersistActionBulkQueue getQueue(String mapName) {
		PersistActionBulkQueue queue = this.queues.get(mapName);
		if(queue == null)
			queue = newQueue(mapName);
		return queue;
	}
	
	protected PersistActionBulkQueue newQueue(String mapName) {
		synchronized (queues) {
			PersistActionBulkQueue queue = this.queues.get(mapName);
			if(queue == null) {
				queue = new PersistActionBulkQueue();
				queues.put(mapName, queue);
			}
			return queue;
		}
	}
	
	public void addTicket(PersistActionBulkQueue queue) {
		this.ticketQueue.add(queue);
	}
	
	public PersistActionBulkQueue takeTicket() throws InterruptedException {
		return ticketQueue.take();
	}
	
}
