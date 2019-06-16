package com.tvd12.calabash.local.persist;

import java.util.concurrent.ExecutorService;

import com.tvd12.ezyfox.builder.EzyBuilder;
import com.tvd12.ezyfox.concurrent.EzyExecutors;
import com.tvd12.ezyfox.util.EzyLoggable;
import com.tvd12.ezyfox.util.EzyStartable;
import com.tvd12.ezyfox.util.EzyStoppable;

public class PersistActionBulkHandlingLoop
		extends EzyLoggable
		implements EzyStartable, EzyStoppable {

	protected volatile boolean active;
	protected final ExecutorService executorService;
	protected final PersistActionBulkTicketQueues ticketQueues;
	
	protected PersistActionBulkHandlingLoop(Builder builder) {
		this.ticketQueues = builder.ticketQueues;
		this.executorService = newExecutorService();
	}
	
	protected ExecutorService newExecutorService() {
		ExecutorService executorService = EzyExecutors.newFixedThreadPool(8, "calabash-bulk-persit");
		Runtime.getRuntime().addShutdownHook(new Thread(() -> executorService.shutdown()));
		return executorService;
	}
	
	@Override
	public void start() throws Exception {
		this.executorService.submit(this::loop);
	}
	
	@Override
	public void stop() {
		this.active = false;
		this.executorService.shutdown();
	}
	
	protected void loop() {
		this.active = true;
		while(active) {
			handle();
		}
	}
	
	protected void handle() {
		PersistActionBulk bulk = null;
		try {
			PersistActionBulkQueue queue = ticketQueues.takeTicket();
			synchronized (queue) {
				bulk = queue.poll();
				if(queue.size() > 0)
					ticketQueues.addTicket(queue);
			}
			bulk.execute();
		}
		catch (Exception e) {
			logger.error("persist bulk error", e);
		}
	}
	
	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder implements EzyBuilder<PersistActionBulkHandlingLoop> {
		
		protected PersistActionBulkTicketQueues ticketQueues;
		
		public Builder ticketQueues(PersistActionBulkTicketQueues ticketQueues) {
			this.ticketQueues = ticketQueues;
			return this;
		}
		
		@Override
		public PersistActionBulkHandlingLoop build() {
			return new PersistActionBulkHandlingLoop(this);
		}
		
	}
}
