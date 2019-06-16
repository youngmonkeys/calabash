package com.tvd12.calabash.local.persist;

import java.util.List;

import com.tvd12.calabash.core.EntityMapPersist;

@SuppressWarnings("rawtypes")
public abstract class PersistAbstractActionBulk implements PersistActionBulk {

	protected final EntityMapPersist mapPersist;
	protected final List<PersistAction> actions;
	
	protected PersistAbstractActionBulk(Builder builder) {
		this.actions = builder.actions;
		this.mapPersist = builder.mapPersist;
	}
	
	public abstract static class Builder implements PersistActionBulkBuilder {
		
		protected EntityMapPersist mapPersist;
		protected List<PersistAction> actions;
		
		public Builder mapPersist(EntityMapPersist mapPersist) {
			this.mapPersist = mapPersist;
			return this;
		}
		
		public Builder actions(List<PersistAction> actions) {
			this.actions = actions;
			return this;
		}
		
	}

}
