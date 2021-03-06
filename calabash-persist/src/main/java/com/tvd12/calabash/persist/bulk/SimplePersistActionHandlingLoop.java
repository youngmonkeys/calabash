package com.tvd12.calabash.persist.bulk;

import com.tvd12.calabash.persist.handler.PersistActionHandlingLoop;

public class SimplePersistActionHandlingLoop extends PersistActionHandlingLoop {
	
	protected SimplePersistActionHandlingLoop(Builder builder) {
		super(builder);
	}
	
	@Override
	protected PersistActionBulkFactory newPersistActionBulkFactory() {
		return new SimplePersistActionBulkFactory();
	}

	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder extends PersistActionHandlingLoop.Builder {
		
		@Override
		public PersistActionHandlingLoop build() {
			return new SimplePersistActionHandlingLoop(this);
		}
	}
	
}
