package com.tvd12.calabash.core.persist;

import java.util.LinkedList;
import java.util.Queue;

public class PersistActionBulkQueue {

	protected final Queue<PersistActionBulk> queue;
	
	public PersistActionBulkQueue() {
		this.queue = new LinkedList<>();
	}
	
	public void add(PersistActionBulk bulk) {
		this.queue.offer(bulk);
	}
	
	public PersistActionBulk poll() {
		PersistActionBulk bulk = queue.poll();
		return bulk;
	}
	
	public int size() {
		int size = queue.size();
		return size;
	}
	
	public boolean isEmpty() {
		boolean empty = queue.isEmpty();
		return empty;
	}
	
}
