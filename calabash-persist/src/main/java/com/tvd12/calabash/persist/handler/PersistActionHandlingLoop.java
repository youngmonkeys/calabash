package com.tvd12.calabash.persist.handler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.tvd12.calabash.persist.action.PersistAction;
import com.tvd12.calabash.persist.action.PersistActionQueue;
import com.tvd12.calabash.persist.action.PersistActionQueueManager;
import com.tvd12.calabash.persist.action.PersistActionType;
import com.tvd12.calabash.persist.bulk.PersistActionBulk;
import com.tvd12.calabash.persist.bulk.PersistActionBulkBuilder;
import com.tvd12.calabash.persist.bulk.PersistActionBulkFactory;
import com.tvd12.calabash.persist.bulk.PersistActionBulkQueue;
import com.tvd12.calabash.persist.bulk.PersistActionBulkTicketQueues;
import com.tvd12.calabash.persist.manager.MapPersistManager;
import com.tvd12.ezyfox.builder.EzyBuilder;
import com.tvd12.ezyfox.concurrent.EzyExecutors;
import com.tvd12.ezyfox.util.EzyLoggable;
import com.tvd12.ezyfox.util.EzyStartable;
import com.tvd12.ezyfox.util.EzyStoppable;

public abstract class PersistActionHandlingLoop
		extends EzyLoggable 
		implements EzyStartable, EzyStoppable {

	protected final MapPersistManager mapPersistManager;
	protected final PersistActionQueueManager actionQueueManager;
	protected final PersistActionBulkFactory actionBulkFactory;
	protected final PersistActionBulkTicketQueues bulkTicketQueues;
	protected final PersistActionBulkHandlingLoop bulkHandlingLoop;
	protected final ScheduledExecutorService delayedPersistSchedule;
	protected final ScheduledExecutorService immediatePersistSchedule;
	
	public PersistActionHandlingLoop(Builder builder) {
		this.mapPersistManager = builder.mapPersistManager;
		this.actionQueueManager = builder.actionQueueManager;
		this.actionBulkFactory = newPersistActionBulkFactory();
		this.bulkTicketQueues = new PersistActionBulkTicketQueues();
		this.delayedPersistSchedule = newDelayedPersistSchedule();
		this.immediatePersistSchedule = newImmediatePersistSchedule();
		this.bulkHandlingLoop = newBulkHandlingLoop();
	}
	
	protected abstract PersistActionBulkFactory newPersistActionBulkFactory();
	
	private PersistActionBulkHandlingLoop newBulkHandlingLoop() {
		return PersistActionBulkHandlingLoop.builder()
				.ticketQueues(bulkTicketQueues)
				.build();
	}
	
	private ScheduledExecutorService newDelayedPersistSchedule() {
		ScheduledExecutorService service = EzyExecutors.newSingleThreadScheduledExecutor("calabash-delayed-persist-loop");
		Runtime.getRuntime().addShutdownHook(new Thread(service::shutdown));
		return service;
	}
	
	private ScheduledExecutorService newImmediatePersistSchedule() {
		ScheduledExecutorService service = EzyExecutors.newSingleThreadScheduledExecutor("calabash-immediate-persist-loop");
		Runtime.getRuntime().addShutdownHook(new Thread(service::shutdown));
		return service;
	}
	
	@Override
	public void start() throws Exception {
		this.delayedPersistSchedule.scheduleAtFixedRate(
				this::handleDelayedQueues, 300, 300, TimeUnit.MILLISECONDS);
		this.delayedPersistSchedule.scheduleAtFixedRate(
				this::handleImmediateQueues, 100, 100, TimeUnit.MILLISECONDS);
		this.bulkHandlingLoop.start();
	}
	
	@Override
	public void stop() {
		this.delayedPersistSchedule.shutdown();
		this.immediatePersistSchedule.shutdown();
		this.bulkHandlingLoop.stop();
	}
	
	protected void handleDelayedQueues() {
		Map<String, PersistActionQueue> readyQueues = actionQueueManager.getReadyDelayedQueues();
		for(String mapName : readyQueues.keySet()) {
			PersistActionQueue queue = readyQueues.get(mapName);
			handleQueue(mapName, queue);
		}
	}
	
	protected void handleImmediateQueues() {
		Map<String, PersistActionQueue> readyQueues = actionQueueManager.getReadyImmediateQueues();
		for(String mapName : readyQueues.keySet()) {
			PersistActionQueue queue = readyQueues.get(mapName);
			handleQueue(mapName, queue);
		}
	}
	
	protected void handleQueue(String mapName, PersistActionQueue queue) {
		try {
			handleQueue0(mapName, queue);
		}
		catch (Exception e) {
			logger.warn("handle queue of map: {} error", mapName, e);
		}
	}
	
	protected void handleQueue0(String mapName, PersistActionQueue queue) {
		Object mapPersist = mapPersistManager.getMapPersist(mapName);
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
			boolean sameType = currentActionType.sames(actionType);
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
	
	public abstract static class Builder implements EzyBuilder<PersistActionHandlingLoop> {
		
		protected MapPersistManager mapPersistManager;
		protected PersistActionQueueManager actionQueueManager;
		
		public Builder mapPersistManager(MapPersistManager mapPersistManager) {
			this.mapPersistManager = mapPersistManager;
			return this;
		}
		
		public Builder actionQueueManager(PersistActionQueueManager actionQueueManager) {
			this.actionQueueManager = actionQueueManager;
			return this;
		}
	}
	
}
