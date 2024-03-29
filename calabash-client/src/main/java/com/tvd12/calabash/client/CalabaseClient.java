package com.tvd12.calabash.client;

import com.tvd12.calabash.Calabash;
import com.tvd12.calabash.client.factory.AtomicLongFactory;
import com.tvd12.calabash.client.factory.EntityMapFactory;
import com.tvd12.calabash.client.manager.AtomicLongProvider;
import com.tvd12.calabash.client.manager.EntityMapProvider;
import com.tvd12.calabash.client.manager.MessageChannelProvider;
import com.tvd12.calabash.client.setting.Settings;
import com.tvd12.calabash.core.EntityMap;
import com.tvd12.calabash.core.IAtomicLong;
import com.tvd12.ezyfox.builder.EzyBuilder;
import com.tvd12.ezyfox.codec.EzyEntityCodec;

public class CalabaseClient implements Calabash {

    protected final Settings settings;
    protected final CalabashClientProxy clientProxy;
    protected final EzyEntityCodec entityCodec;
    protected final EntityMapFactory mapFactory;
    protected final EntityMapProvider mapProvider;
    protected final AtomicLongFactory atomicLongFactory;
    protected final AtomicLongProvider atomicLongProvider;
    protected final MessageChannelProvider messageChannelProvider;

    protected CalabaseClient(Builder builder) {
        this.settings = builder.settings;
        this.clientProxy = builder.clientProxy;
        this.entityCodec = builder.entityCodec;
        this.mapFactory = newMapFactory();
        this.mapProvider = newMapProvider();
        this.atomicLongFactory = newAtomicLongFactory();
        this.atomicLongProvider = newAtomicLongProvider();
        this.messageChannelProvider = builder.messageChannelProvider;
    }

    public static Builder builder() {
        return new Builder();
    }

    protected EntityMapFactory newMapFactory() {
        return EntityMapFactory.builder()
            .settings(settings)
            .clientProxy(clientProxy)
            .entityCodec(entityCodec)
            .build();
    }

    protected EntityMapProvider newMapProvider() {
        return EntityMapProvider.builder()
            .mapFactory(mapFactory)
            .build();
    }

    protected AtomicLongFactory newAtomicLongFactory() {
        return AtomicLongFactory.builder()
            .settings(settings)
            .clientProxy(clientProxy)
            .build();
    }

    protected AtomicLongProvider newAtomicLongProvider() {
        return AtomicLongProvider.builder()
            .atomicLongFactory(atomicLongFactory)
            .build();
    }

    @Override
    public <K, V> EntityMap<K, V> getEntityMap(String name) {
        return getMap(name);
    }

    public <K, V> EntityMap<K, V> getMap(String name) {
        return mapProvider.getMap(name);
    }

    public <K, V> EntityMap<K, V> getMap(
        String name, Class<K> keyType, Class<V> valueType) {
        return mapProvider.getMap(name, keyType, valueType);
    }

    public <T> MessageChannel<T> getMessageChannel(String name) {
        return messageChannelProvider.getChannel(name);
    }

    public <T> MessageChannel<T> getMessageChannel(String name, Class<T> messageType) {
        return messageChannelProvider.getChannel(name, messageType);
    }

    @Override
    public IAtomicLong getAtomicLong(String name) {
        return atomicLongProvider.getAtomicLong(name);
    }

    public static class Builder implements EzyBuilder<CalabaseClient> {

        protected Settings settings;
        protected EzyEntityCodec entityCodec;
        protected CalabashClientProxy clientProxy;
        protected MessageChannelProvider messageChannelProvider;

        public Builder settings(Settings settings) {
            this.settings = settings;
            return this;
        }

        public Builder entityCodec(EzyEntityCodec entityCodec) {
            this.entityCodec = entityCodec;
            return this;
        }

        public Builder clientProxy(CalabashClientProxy clientProxy) {
            this.clientProxy = clientProxy;
            return this;
        }

        public Builder messageChannelProvider(MessageChannelProvider messageChannelProvider) {
            this.messageChannelProvider = messageChannelProvider;
            return this;
        }

        @Override
        public CalabaseClient build() {
            return new CalabaseClient(this);
        }
    }
}
