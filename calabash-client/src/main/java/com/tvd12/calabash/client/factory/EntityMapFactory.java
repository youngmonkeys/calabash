package com.tvd12.calabash.client.factory;

import com.tvd12.calabash.client.CalabashClientProxy;
import com.tvd12.calabash.client.impl.EntityMapImpl;
import com.tvd12.calabash.client.setting.EntityMapSetting;
import com.tvd12.calabash.client.setting.EntityMapSettingBuilder;
import com.tvd12.calabash.client.setting.Settings;
import com.tvd12.calabash.core.EntityMap;
import com.tvd12.ezyfox.builder.EzyBuilder;
import com.tvd12.ezyfox.codec.EzyEntityCodec;

public class EntityMapFactory {

    protected final Settings settings;
    protected final EzyEntityCodec entityCodec;
    protected final CalabashClientProxy clientProxy;

    protected EntityMapFactory(Builder builder) {
        this.settings = builder.settings;
        this.clientProxy = builder.clientProxy;
        this.entityCodec = builder.entityCodec;
    }

    public static Builder builder() {
        return new Builder();
    }

    public <K, V> EntityMap<K, V> newMap(
        String name, Class<K> keyType, Class<V> valueType) {
        EntityMapSetting mapSetting = new EntityMapSettingBuilder()
            .keyType(keyType)
            .valueType(valueType)
            .build();
        return newMap(name, mapSetting);
    }

    public <K, V> EntityMap<K, V> newMap(String name) {
        EntityMapSetting mapSetting = settings.getMapSeting(name);
        return newMap(name, mapSetting);
    }

    @SuppressWarnings("unchecked")
    private <K, V> EntityMap<K, V> newMap(
        String name, EntityMapSetting mapSetting) {
        if (mapSetting == null) {
            throw new IllegalArgumentException("has no setting for map: " + name);
        }
        return EntityMapImpl.builder()
            .mapName(name)
            .setting(mapSetting)
            .clientProxy(clientProxy)
            .entityCodec(entityCodec)
            .build();
    }

    public static class Builder implements EzyBuilder<EntityMapFactory> {

        protected Settings settings;
        protected EzyEntityCodec entityCodec;
        protected CalabashClientProxy clientProxy;

        public Builder settings(Settings settings) {
            this.settings = settings;
            return this;
        }

        public Builder clientProxy(CalabashClientProxy redisClient) {
            this.clientProxy = redisClient;
            return this;
        }

        public Builder entityCodec(EzyEntityCodec entityCodec) {
            this.entityCodec = entityCodec;
            return this;
        }

        @Override
        public EntityMapFactory build() {
            return new EntityMapFactory(this);
        }
    }
}
