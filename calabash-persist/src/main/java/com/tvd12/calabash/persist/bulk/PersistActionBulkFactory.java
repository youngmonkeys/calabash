package com.tvd12.calabash.persist.bulk;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import com.tvd12.calabash.persist.action.PersistActionType;

public abstract class PersistActionBulkFactory {
	
	protected final Map<PersistActionType, Supplier<PersistActionBulkBuilder>> builderSuppliers;
	
	public PersistActionBulkFactory() {
		this.builderSuppliers = defaultBuilderSuppliers();
	}

	public PersistActionBulkBuilder newBulkBuilder(PersistActionType actionType) {
		Supplier<PersistActionBulkBuilder> supplier = builderSuppliers.get(actionType);
		PersistActionBulkBuilder builder = supplier.get();
		return builder;
	}
	
	private Map<PersistActionType, Supplier<PersistActionBulkBuilder>> defaultBuilderSuppliers() {
		Map<PersistActionType, Supplier<PersistActionBulkBuilder>> map = new HashMap<>();
		addDefaultBuilderSuppliers(map);
		return map;
	}
	
	protected abstract void addDefaultBuilderSuppliers(
			Map<PersistActionType, Supplier<PersistActionBulkBuilder>> map);
}
