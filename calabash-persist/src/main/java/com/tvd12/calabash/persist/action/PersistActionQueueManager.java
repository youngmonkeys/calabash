package com.tvd12.calabash.persist.action;

import java.util.HashMap;
import java.util.Map;

public class PersistActionQueueManager {

    protected final PersistActionQueueFactory queueFactory;
    protected final Map<String, PersistActionQueue> delayedQueues;
    protected final Map<String, PersistActionQueue> immediateQueues;

    public PersistActionQueueManager(PersistActionQueueFactory queueFactory) {
        this.queueFactory = queueFactory;
        this.delayedQueues = new HashMap<>();
        this.immediateQueues = new HashMap<>();
    }

    public PersistActionQueue getDelayedQueue(String mapName) {
        PersistActionQueue queue = delayedQueues.get(mapName);
        if (queue == null) {
            queue = newQueue(mapName, delayedQueues);
        }
        return queue;
    }

    public PersistActionQueue getImmediateQueue(String mapName) {
        PersistActionQueue queue = immediateQueues.get(mapName);
        if (queue == null) {
            queue = newQueue(mapName, immediateQueues);
        }
        return queue;
    }

    protected PersistActionQueue newQueue(
        String mapName,
        Map<String, PersistActionQueue> queues
    ) {
        //noinspection SynchronizationOnLocalVariableOrMethodParameter
        synchronized (queues) {
            PersistActionQueue queue = queues.get(mapName);
            if (queue == null) {
                queue = queueFactory.newActionQueue(mapName);
                queues.put(mapName, queue);
            }
            return queue;
        }
    }

    public Map<String, PersistActionQueue> getReadyDelayedQueues() {
        Map<String, PersistActionQueue> readyQueues = new HashMap<>();
        synchronized (delayedQueues) {
            for (String mapName : delayedQueues.keySet()) {
                PersistActionQueue queue = delayedQueues.get(mapName);
                if (queue.isReady()) {
                    readyQueues.put(mapName, queue);
                }
            }
        }
        return readyQueues;
    }

    public Map<String, PersistActionQueue> getReadyImmediateQueues() {
        Map<String, PersistActionQueue> readyQueues = new HashMap<>();
        synchronized (immediateQueues) {
            for (String mapName : immediateQueues.keySet()) {
                PersistActionQueue queue = immediateQueues.get(mapName);
                if (queue.size() > 0) {
                    readyQueues.put(mapName, queue);
                }
            }
        }
        return readyQueues;
    }

}
