package com.tvd12.calabash.persist.bulk;

import com.tvd12.calabash.persist.action.PersistActionType;

import java.util.Map;
import java.util.function.Supplier;

public class SimplePersistActionBulkFactory extends PersistActionBulkFactory {

    @Override
    protected void addDefaultBuilderSuppliers(Map<PersistActionType, Supplier<PersistActionBulkBuilder>> map) {
        map.put(PersistActionType.SAVE_ONE, PersistSaveActionBulk::builder);
        map.put(PersistActionType.SAVE_MANY, PersistSaveActionBulk::builder);
        map.put(PersistActionType.DELETE_ONE, PersistDeleteActionBulk::builder);
        map.put(PersistActionType.DELETE_MANY, PersistDeleteActionBulk::builder);
    }
}
