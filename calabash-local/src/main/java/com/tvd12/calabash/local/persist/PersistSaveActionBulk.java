package com.tvd12.calabash.local.persist;

import com.tvd12.calabash.persist.EntityMapPersist;
import com.tvd12.calabash.persist.action.PersistAction;
import com.tvd12.calabash.persist.action.PersistActionType;
import com.tvd12.calabash.persist.action.PersistSaveManyAction;
import com.tvd12.calabash.persist.action.PersistSaveOneAction;
import com.tvd12.calabash.persist.bulk.PersistAbstractActionBulk;
import com.tvd12.calabash.persist.bulk.PersistActionBulk;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings({"rawtypes", "unchecked"})
public class PersistSaveActionBulk extends PersistAbstractActionBulk {

    public PersistSaveActionBulk(Builder builder) {
        super(builder);
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public void execute() {
        Map<Object, Object> keyValues = new HashMap<>();
        for (PersistAction action : actions) {
            if (action.getType() == PersistActionType.SAVE_ONE) {
                PersistSaveOneAction one = (PersistSaveOneAction) action;
                keyValues.put(one.getKey(), one.getValue());
            } else {
                PersistSaveManyAction many = (PersistSaveManyAction) action;
                keyValues.putAll(many.getKeyValues());
            }
        }
        ((EntityMapPersist) mapPersist).persist(keyValues);
    }

    public static class Builder extends PersistAbstractActionBulk.Builder {

        @Override
        public PersistActionBulk build() {
            return new PersistSaveActionBulk(this);
        }
    }
}
