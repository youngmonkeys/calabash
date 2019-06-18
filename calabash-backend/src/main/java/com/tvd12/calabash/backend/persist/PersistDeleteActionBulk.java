package com.tvd12.calabash.backend.persist;

import java.util.HashSet;
import java.util.Set;

import com.tvd12.calabash.core.persist.PersistAction;
import com.tvd12.calabash.core.persist.PersistActionBulk;
import com.tvd12.calabash.core.util.ByteArray;

public class PersistDeleteActionBulk extends PersistAbstractActionBulk {

	public PersistDeleteActionBulk(Builder builder) {
		super(builder);
	}

	@Override
	public void execute() {
		Set<ByteArray> keys = new HashSet<>();
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
		mapPersist.delete(keys);
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
