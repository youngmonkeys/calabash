package com.tvd12.calabash.client.impl;

import com.tvd12.calabash.client.CalabashClientProxy;
import com.tvd12.calabash.core.IAtomicLong;
import com.tvd12.ezyfox.builder.EzyBuilder;

public class AtomicLongImpl implements IAtomicLong {

	protected final int id;
	protected final String name;
	protected final CalabashClientProxy clientProxy;

	protected AtomicLongImpl(Builder builder) {
		this.name = builder.name;
		this.clientProxy = builder.redisClient;
		this.id = clientProxy.atomicLongGetId(name);
	}
	
	@Override
	public long get() {
		Long answer = clientProxy.atomicLongGet(id);
		return answer;
	}
	
	@Override
	public long addAndGet(long delta) {
		long answer = clientProxy.atomicLongAddAndGet(id, delta);
		return answer;
	}
	
	@Override
	public long incrementAndGet() {
		long answer = clientProxy.atomicLongIncrementAndGet(id);
		return answer;
	}
	
	public String getName() {
		return name;
	}
	
	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder implements EzyBuilder<AtomicLongImpl> {
		
		protected String name;
		protected String mapName;
		protected CalabashClientProxy redisClient;
		
		public Builder name(String name) {
			this.name = name;
			return this;
		}
		
		public Builder mapName(String mapName) {
			this.mapName = mapName;
			return this;
		}
		
		public Builder redisClient(CalabashClientProxy redisClient) {
			this.redisClient = redisClient;
			return this;
		}
		
		@Override
		public AtomicLongImpl build() {
			return new AtomicLongImpl(this);
		}
		
		
	}
}
