package com.tvd12.calabash.local.persist;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import com.tvd12.calabash.core.persist.PersistActionType;

public class PersistActionBulkFactory {
	
	protected final Map<PersistActionType, Supplier<PersistActionBulkBuilder>> builderSuppliers;
	
	public PersistActionBulkFactory() {
		this.builderSuppliers = defaultBuilderSuppliers();
	}

	public PersistActionBulkBuilder newBulkBuilder(PersistActionType actionType) {
		Supplier<PersistActionBulkBuilder> supplier = builderSuppliers.get(actionType);
		PersistActionBulkBuilder builder = supplier.get();
		return builder;
	}
	
	protected Map<PersistActionType, Supplier<PersistActionBulkBuilder>> defaultBuilderSuppliers() {
		Map<PersistActionType, Supplier<PersistActionBulkBuilder>> map = new HashMap<>();
		map.put(PersistActionType.SAVE_ONE, PersistSaveActionBulk::builder);
		map.put(PersistActionType.SAVE_MANY, PersistSaveActionBulk::builder);
		return map;
	}
}
