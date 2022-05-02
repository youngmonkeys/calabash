package com.tvd12.calabash.local.persist;

import com.tvd12.calabash.persist.EntityMapPersist;
import com.tvd12.calabash.persist.action.PersistAction;
import com.tvd12.calabash.persist.action.PersistActionType;
import com.tvd12.calabash.persist.action.PersistDeleteManyAction;
import com.tvd12.calabash.persist.action.PersistDeleteOneAction;
import com.tvd12.calabash.persist.bulk.PersistAbstractActionBulk;
import com.tvd12.calabash.persist.bulk.PersistActionBulk;

import java.util.HashSet;
import java.util.Set;

@SuppressWarnings({"rawtypes", "unchecked"})
public class PersistDeleteActionBulk extends PersistAbstractActionBulk {

    public PersistDeleteActionBulk(Builder builder) {
        super(builder);
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public void execute() {
        Set<Object> keys = new HashSet<>();
        for (PersistAction action : actions) {
            if (action.getType() == PersistActionType.DELETE_ONE) {
                keys.add(((PersistDeleteOneAction) action).getKey());
            } else {
                keys.addAll(((PersistDeleteManyAction) action).getKeys());
            }
        }
        ((EntityMapPersist) mapPersist).delete(keys);
    }

    public static class Builder extends PersistAbstractActionBulk.Builder {

        @Override
        public PersistActionBulk build() {
            return new PersistDeleteActionBulk(this);
        }
    }
}
