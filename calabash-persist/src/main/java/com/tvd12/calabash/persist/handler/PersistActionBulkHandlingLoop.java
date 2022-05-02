package com.tvd12.calabash.persist.handler;

import com.tvd12.calabash.concurrent.Executors;
import com.tvd12.calabash.persist.bulk.PersistActionBulk;
import com.tvd12.calabash.persist.bulk.PersistActionBulkQueue;
import com.tvd12.calabash.persist.bulk.PersistActionBulkTicketQueues;
import com.tvd12.ezyfox.builder.EzyBuilder;
import com.tvd12.ezyfox.concurrent.EzyThreadList;
import com.tvd12.ezyfox.util.EzyLoggable;
import com.tvd12.ezyfox.util.EzyStartable;
import com.tvd12.ezyfox.util.EzyStoppable;

public class PersistActionBulkHandlingLoop
    extends EzyLoggable
    implements EzyStartable, EzyStoppable {

    protected final int threadPoolSize;
    protected final EzyThreadList executorService;
    protected final PersistActionBulkTicketQueues ticketQueues;
    protected volatile boolean active;

    protected PersistActionBulkHandlingLoop(Builder builder) {
        this.ticketQueues = builder.ticketQueues;
        this.threadPoolSize = builder.threadPoolSize;
        this.executorService = newExecutorService();
    }

    public static Builder builder() {
        return new Builder();
    }

    protected EzyThreadList newExecutorService() {
        return new EzyThreadList(
            threadPoolSize,
            this::loop,
            Executors.newThreadFactory("persist-bulk"));
    }

    @Override
    public void start() {
        this.executorService.execute();
    }

    @Override
    public void stop() {
        this.active = false;
    }

    protected void loop() {
        this.active = true;
        while (active) {
            handle();
        }
    }

    protected void handle() {
        PersistActionBulk bulk;
        try {
            PersistActionBulkQueue queue = ticketQueues.takeTicket();
            //noinspection SynchronizationOnLocalVariableOrMethodParameter
            synchronized (queue) {
                bulk = queue.poll();
                if (queue.size() > 0) {
                    ticketQueues.addTicket(queue);
                }
            }
            bulk.execute();
        } catch (Exception e) {
            logger.error("persist bulk error", e);
        }
    }

    public static class Builder implements EzyBuilder<PersistActionBulkHandlingLoop> {

        protected int threadPoolSize = 3;
        protected PersistActionBulkTicketQueues ticketQueues = null;

        public Builder threadPoolSize(int threadPoolSize) {
            this.threadPoolSize = threadPoolSize;
            return this;
        }

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
