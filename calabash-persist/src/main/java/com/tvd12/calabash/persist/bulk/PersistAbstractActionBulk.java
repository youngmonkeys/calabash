package com.tvd12.calabash.persist.bulk;

import com.tvd12.calabash.persist.action.PersistAction;

import java.util.List;

public abstract class PersistAbstractActionBulk implements PersistActionBulk {

    protected final Object mapPersist;
    protected final List<PersistAction> actions;

    protected PersistAbstractActionBulk(Builder builder) {
        this.actions = builder.actions;
        this.mapPersist = builder.mapPersist;
    }

    public abstract static class Builder implements PersistActionBulkBuilder {

        protected Object mapPersist;
        protected List<PersistAction> actions;

        @Override
        public Builder mapPersist(Object mapPersist) {
            this.mapPersist = mapPersist;
            return this;
        }

        @Override
        public Builder actions(List<PersistAction> actions) {
            this.actions = actions;
            return this;
        }
    }
}
