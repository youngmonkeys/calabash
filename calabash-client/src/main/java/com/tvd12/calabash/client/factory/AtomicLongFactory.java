package com.tvd12.calabash.client.factory;

import com.tvd12.calabash.client.CalabashClientProxy;
import com.tvd12.calabash.client.impl.AtomicLongImpl;
import com.tvd12.calabash.client.setting.Settings;
import com.tvd12.calabash.core.IAtomicLong;
import com.tvd12.ezyfox.builder.EzyBuilder;
import com.tvd12.ezyfox.io.EzyStrings;

public class AtomicLongFactory {

    protected final Settings settings;
    protected final CalabashClientProxy clientProxy;

    protected AtomicLongFactory(Builder builder) {
        this.settings = builder.settings;
        this.clientProxy = builder.clientProxy;
    }

    public static Builder builder() {
        return new Builder();
    }

    public IAtomicLong newAtomicLong(String name) {
        String mapName = settings.getAtomicLongMapName();
        if (EzyStrings.isNoContent(mapName)) {
            throw new IllegalArgumentException("has no setting for atomic long map name");
        }
        return AtomicLongImpl.builder()
            .name(name)
            .redisClient(clientProxy)
            .mapName(mapName)
            .build();
    }

    public static class Builder implements EzyBuilder<AtomicLongFactory> {

        protected Settings settings;
        protected CalabashClientProxy clientProxy;

        public Builder settings(Settings settings) {
            this.settings = settings;
            return this;
        }

        public Builder clientProxy(CalabashClientProxy clientProxy) {
            this.clientProxy = clientProxy;
            return this;
        }

        @Override
        public AtomicLongFactory build() {
            return new AtomicLongFactory(this);
        }
    }
}
