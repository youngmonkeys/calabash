package com.tvd12.calabash.backend.persist;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.tvd12.calabash.backend.BytesMapPersist;
import com.tvd12.calabash.backend.manager.BytesMapPersistManager;
import com.tvd12.ezyfox.builder.EzyBuilder;
import com.tvd12.ezyfox.concurrent.EzyExecutors;
import com.tvd12.ezyfox.util.EzyLoggable;
import com.tvd12.ezyfox.util.EzyStartable;
import com.tvd12.ezyfox.util.EzyStoppable;

public class PersistActionHandlingLoop
		extends EzyLoggable 
		implements EzyStartable, EzyStoppable {

	protected final BytesMapPersistManager mapPersistManager;
	protected final PersistActionQueueManager actionQueueManager;
	protected final PersistActionBulkFactory actionBulkFactory;
	protected final PersistActionBulkTicketQueues bulkTicketQueues;
	protected final PersistActionBulkHandlingLoop bulkHandlingLoop;
	protected final ScheduledExecutorService scheduledExecutorService;
	
	public PersistActionHandlingLoop(Builder builder) {
		this.mapPersistManager = builder.mapPersistManager;
		this.actionQueueManager = builder.actionQueueManager;
		this.actionBulkFactory = new PersistActionBulkFactory();
		this.bulkTicketQueues = new PersistActionBulkTicketQueues();
		this.scheduledExecutorService = newScheduledExecutorService();
		this.bulkHandlingLoop = newBulkHandlingLoop();
	}
	
	protected PersistActionBulkHandlingLoop newBulkHandlingLoop() {
		return PersistActionBulkHandlingLoop.builder()
				.ticketQueues(bulkTicketQueues)
				.build();
	}
	
	protected ScheduledExecutorService newScheduledExecutorService() {
		ScheduledExecutorService service = EzyExecutors.newSingleThreadScheduledExecutor("calabash-persist-loop");
		Runtime.getRuntime().addShutdownHook(new Thread(() -> service.shutdown()));
		return service;
	}
	
	@Override
	public void start() throws Exception {
		this.scheduledExecutorService.scheduleAtFixedRate(this::handle, 100, 100, TimeUnit.MILLISECONDS);
		this.bulkHandlingLoop.start();
	}
	
	@Override
	public void stop() {
		this.scheduledExecutorService.shutdown();
		this.bulkHandlingLoop.stop();
	}
	
	protected void handle() {
		Map<String, PersistActionQueue> readyQueues = actionQueueManager.getReadyQueues();
		for(String mapName : readyQueues.keySet()) {
			PersistActionQueue queue = readyQueues.get(mapName);
			handleQueue(mapName, queue);
		}
	}
	
	protected void handleQueue(String mapName, PersistActionQueue queue) {
		BytesMapPersist mapPersist = mapPersistManager.getMapPersist(mapName);
		List<PersistAction> actions = queue.polls();
		List<PersistAction> sameActions = null;
		PersistActionType currentActionType = null;
		int size = actions.size();
		int last = size - 1;
		for(int i = 0 ; i < actions.size() ; i++) {
			PersistAction action = actions.get(i);
			PersistActionType actionType = action.getType();
			if(currentActionType == null) {
				currentActionType = actionType;
				sameActions = new ArrayList<>();
			}
			boolean sameType = currentActionType.equals(actionType);
			if(sameType)
				sameActions.add(action);
			if(!sameType || i == last) {
				PersistActionBulkBuilder builder = actionBulkFactory.newBulkBuilder(currentActionType);
				PersistActionBulk bulk = builder
						.mapPersist(mapPersist)
						.actions(sameActions)
						.build();
				currentActionType = actionType;
				sameActions = new ArrayList<>();
				PersistActionBulkQueue bulkQueue = bulkTicketQueues.getQueue(mapName);
				synchronized (bulkQueue) {
					boolean empty = bulkQueue.isEmpty();
					bulkQueue.add(bulk);
					if(empty)
						bulkTicketQueues.addTicket(bulkQueue);
				}
			}
		}
	}
	
	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder implements EzyBuilder<PersistActionHandlingLoop> {
		
		protected BytesMapPersistManager mapPersistManager;
		protected PersistActionQueueManager actionQueueManager;
		
		public Builder mapPersistManager(BytesMapPersistManager mapPersistManager) {
			this.mapPersistManager = mapPersistManager;
			return this;
		}
		
		public Builder actionQueueManager(PersistActionQueueManager actionQueueManager) {
			this.actionQueueManager = actionQueueManager;
			return this;
		}
		
		@Override
		public PersistActionHandlingLoop build() {
			return new PersistActionHandlingLoop(this);
		}
	}
	
}
