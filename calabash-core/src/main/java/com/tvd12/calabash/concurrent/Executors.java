package com.tvd12.calabash.concurrent;

import com.tvd12.calabash.Calabash;
import com.tvd12.ezyfox.concurrent.EzyExecutors;
import com.tvd12.ezyfox.concurrent.EzyThreadFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;

public final class Executors {

    private Executors() {}

    public static ExecutorService newFixedThreadPool(
        int numberOfThreads,
        String threadName
    ) {
        ThreadFactory threadFactory = newThreadFactory(threadName);
        return EzyExecutors.newFixedThreadPool(numberOfThreads, threadFactory);
    }

    public static ScheduledExecutorService newSingleThreadScheduledExecutor(
        String threadName
    ) {
        ThreadFactory threadFactory = newThreadFactory(threadName);
        return EzyExecutors.newSingleThreadScheduledExecutor(threadFactory);
    }

    public static ThreadFactory newThreadFactory(String threadName) {
        return EzyThreadFactory.builder()
            .prefix(Calabash.NAME)
            .poolName(threadName)
            .build();
    }
}
