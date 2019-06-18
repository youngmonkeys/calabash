package com.tvd12.calabash.backend.persist;

import java.util.List;

import com.tvd12.calabash.backend.BytesMapPersist;
import com.tvd12.calabash.core.persist.PersistAction;
import com.tvd12.calabash.core.persist.PersistActionBulk;

public abstract class PersistAbstractActionBulk implements PersistActionBulk {

	protected final BytesMapPersist mapPersist;
	protected final List<PersistAction> actions;
	
	protected PersistAbstractActionBulk(Builder builder) {
		this.actions = builder.actions;
		this.mapPersist = builder.mapPersist;
	}
	
	public abstract static class Builder implements PersistActionBulkBuilder {
		
		protected BytesMapPersist mapPersist;
		protected List<PersistAction> actions;
		
		public Builder mapPersist(BytesMapPersist mapPersist) {
			this.mapPersist = mapPersist;
			return this;
		}
		
		public Builder actions(List<PersistAction> actions) {
			this.actions = actions;
			return this;
		}
		
	}

}
