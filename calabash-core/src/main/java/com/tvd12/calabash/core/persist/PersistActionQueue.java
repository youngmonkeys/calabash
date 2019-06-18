package com.tvd12.calabash.core.persist;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class PersistActionQueue {

	protected final Queue<PersistAction> queue;
	
	public PersistActionQueue() {
		this.queue = new LinkedList<>();
	}
	
	public boolean isReady() {
		synchronized (queue) {
			boolean ready = queue.size() > 0;
			return ready;
		}
	}
	
	public void add(PersistAction action) {
		synchronized (queue) {
			queue.offer(action);
		}
	}
	
	public List<PersistAction> polls() {
		List<PersistAction> actions = new ArrayList<>();
		synchronized (queue) {
			while(queue.size() > 0)
				actions.add(queue.poll());
		}
		return actions;
	}
	
	public int size() {
		synchronized (queue) {
			int size = queue.size();
			return size;
		}
	}
	
}
