package com.tvd12.calabash.persist.bulk;

import com.tvd12.calabash.persist.BytesMapPersist;
import com.tvd12.calabash.persist.action.PersistAction;
import com.tvd12.calabash.persist.action.PersistActionType;
import com.tvd12.calabash.persist.action.PersistDeleteManyAction;
import com.tvd12.calabash.persist.action.PersistDeleteOneAction;

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
        Set keys = new HashSet<>();
        for (PersistAction action : actions) {
            if (action.getType() == PersistActionType.DELETE_ONE) {
                keys.add(((PersistDeleteOneAction) action).getKey());
            } else {
                keys.addAll(((PersistDeleteManyAction) action).getKeys());
            }
        }
        ((BytesMapPersist) mapPersist).delete(keys);
    }

    public static class Builder extends PersistAbstractActionBulk.Builder {

        @Override
        public PersistActionBulk build() {
            return new PersistDeleteActionBulk(this);
        }
    }
}
