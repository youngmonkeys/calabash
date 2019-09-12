package com.tvd12.calabash.server.core.backup;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class BackupActionQueue {

	protected final Queue<BackupAction> queue;
	
	public BackupActionQueue() {
		this.queue = new LinkedList<>();
	}
	
	public void add(BackupAction action) {
		synchronized (queue) {
			queue.offer(action);
		}
	}
	
	public List<BackupAction> polls() {
		List<BackupAction> list = new ArrayList<>();
		synchronized (queue) {
			while(queue.size() > 0)
				list.add(queue.poll());
		}
		return list;
	}
	
}
