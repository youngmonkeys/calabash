package com.tvd12.calabash.local.persist;

import java.util.HashSet;
import java.util.Set;

import com.tvd12.calabash.core.EntityMapPersist;
import com.tvd12.calabash.persist.action.PersistAction;
import com.tvd12.calabash.persist.action.PersistDeleteManyAction;
import com.tvd12.calabash.persist.action.PersistDeleteOneAction;
import com.tvd12.calabash.persist.bulk.PersistAbstractActionBulk;
import com.tvd12.calabash.persist.bulk.PersistActionBulk;

@SuppressWarnings({"rawtypes", "unchecked"})
public class PersistDeleteActionBulk extends PersistAbstractActionBulk {

	public PersistDeleteActionBulk(Builder builder) {
		super(builder);
	}

	@Override
	public void execute() {
		Set<Object> keys = new HashSet<>();
		for(PersistAction action : actions) {
			switch (action.getType()) {
			case DELETE_ONE:
				keys.add(((PersistDeleteOneAction)action).getKey());
				break;
			default:
				keys.addAll(((PersistDeleteManyAction)action).getKeys());
				break;
			}
		}
		((EntityMapPersist)mapPersist).delete(keys);
	}
	
	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder extends PersistAbstractActionBulk.Builder {
		
		@Override
		public PersistActionBulk build() {
			return new PersistDeleteActionBulk(this);
		}
		
	}

}
