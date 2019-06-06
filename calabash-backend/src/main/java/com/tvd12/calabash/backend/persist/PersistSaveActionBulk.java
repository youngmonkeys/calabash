package com.tvd12.calabash.backend.persist;

import java.util.HashMap;
import java.util.Map;

import com.tvd12.calabash.core.util.ByteArray;

public class PersistSaveActionBulk extends PersistAbstractActionBulk {

	public PersistSaveActionBulk(Builder builder) {
		super(builder);
	}

	@Override
	public void execute() {
		Map<ByteArray, byte[]> keyValues = new HashMap<>();
		for(PersistAction action : actions) {
			switch (action.getType()) {
			case SAVE_ONE:
				PersistSaveOneAction one = (PersistSaveOneAction)action;
				keyValues.put(one.getKey(), one.getValue());
				break;
			default:
				PersistSaveManyAction many = (PersistSaveManyAction)action;
				keyValues.putAll(many.getKeyValues());
				break;
			}
		}
		mapPersist.persist(keyValues);
	}
	
	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder extends PersistAbstractActionBulk.Builder {
		
		@Override
		public PersistActionBulk build() {
			return new PersistSaveActionBulk(this);
		}
		
	}

}
