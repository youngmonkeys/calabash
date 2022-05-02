package com.tvd12.calabash.persist.bulk;

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
        return queue.poll();
    }

    public int size() {
        return queue.size();
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }
}
