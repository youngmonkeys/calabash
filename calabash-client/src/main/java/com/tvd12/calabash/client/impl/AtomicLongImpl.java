package com.tvd12.calabash.client.impl;

import com.tvd12.calabash.client.CalabashClientProxy;
import com.tvd12.calabash.core.IAtomicLong;
import com.tvd12.ezyfox.builder.EzyBuilder;
import com.tvd12.ezyfox.constant.EzyHasIntId;
import lombok.Getter;

public class AtomicLongImpl implements IAtomicLong, EzyHasIntId {

    @Getter
    protected final int id;
    @Getter
    protected final String name;
    protected final CalabashClientProxy clientProxy;

    protected AtomicLongImpl(Builder builder) {
        this.name = builder.name;
        this.clientProxy = builder.redisClient;
        this.id = clientProxy.atomicLongGetId(name);
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public long get() {
        return clientProxy.atomicLongGet(id);
    }

    @Override
    public long addAndGet(long delta) {
        return clientProxy.atomicLongAddAndGet(id, delta);
    }

    @Override
    public long incrementAndGet() {
        return clientProxy.atomicLongIncrementAndGet(id);
    }

    @Override
    public String toString() {
        return "AtomicLong(" + id + ", " + name + ")";
    }

    public static class Builder implements EzyBuilder<AtomicLongImpl> {

        protected String name;
        protected String mapName;
        protected CalabashClientProxy redisClient;

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder mapName(String mapName) {
            this.mapName = mapName;
            return this;
        }

        public Builder redisClient(CalabashClientProxy redisClient) {
            this.redisClient = redisClient;
            return this;
        }

        @Override
        public AtomicLongImpl build() {
            return new AtomicLongImpl(this);
        }
    }
}
