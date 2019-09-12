package com.tvd12.calabash.server.core.persist;

import com.tvd12.calabash.persist.bulk.PersistActionBulkFactory;
import com.tvd12.calabash.persist.handler.PersistActionHandlingLoop;

public class PersistActionHandlingLoopImpl extends PersistActionHandlingLoop {
	
	protected PersistActionHandlingLoopImpl(Builder builder) {
		super(builder);
	}
	
	@Override
	protected PersistActionBulkFactory newPersistActionBulkFactory() {
		return new PersistActionBulkFactoryImpl();
	}

	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder extends PersistActionHandlingLoop.Builder {
		
		@Override
		public PersistActionHandlingLoop build() {
			return new PersistActionHandlingLoopImpl(this);
		}
	}
	
}
