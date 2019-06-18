package com.tvd12.calabash.concurrent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;

import com.tvd12.calabash.Calabash;
import com.tvd12.ezyfox.concurrent.EzyExecutors;
import com.tvd12.ezyfox.concurrent.EzyThreadFactory;

public final class Executors {

	private Executors() {
	}

	public static ExecutorService newFixedThreadPool(int nThreads, String threadName) {
		ThreadFactory threadFactory = newThreadFactory(threadName);
		ExecutorService service = EzyExecutors.newFixedThreadPool(nThreads, threadFactory);
		return service;
	}
	
	public static ScheduledExecutorService newSingleThreadScheduledExecutor(String threadName) {
		ThreadFactory threadFactory = newThreadFactory(threadName);
		ScheduledExecutorService service = EzyExecutors.newSingleThreadScheduledExecutor(threadFactory);
		return service;
	}
	
	public static ThreadFactory newThreadFactory(String threadName) {
		return EzyThreadFactory.builder()
				.prefix(Calabash.NAME)
				.poolName(threadName)
				.build();
	}

}
