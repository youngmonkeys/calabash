package com.tvd12.calabash.backend.persist;

import java.util.Map;
import java.util.function.Supplier;

import com.tvd12.calabash.persist.action.PersistActionType;
import com.tvd12.calabash.persist.bulk.PersistActionBulkBuilder;
import com.tvd12.calabash.persist.bulk.PersistActionBulkFactory;

public class PersistActionBulkFactoryImpl extends PersistActionBulkFactory {

	@Override
	protected void addDefaultBuilderSuppliers(Map<PersistActionType, Supplier<PersistActionBulkBuilder>> map) {
		map.put(PersistActionType.SAVE_ONE, PersistSaveActionBulk::builder);
		map.put(PersistActionType.SAVE_MANY, PersistSaveActionBulk::builder);
		map.put(PersistActionType.DELETE_ONE, PersistDeleteActionBulk::builder);
		map.put(PersistActionType.DELETE_MANY, PersistDeleteActionBulk::builder);
	}
	
}
