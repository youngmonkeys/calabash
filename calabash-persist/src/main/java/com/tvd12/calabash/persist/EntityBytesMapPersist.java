package com.tvd12.calabash.persist;

import com.tvd12.calabash.core.util.ByteArray;
import com.tvd12.ezyfox.builder.EzyBuilder;
import com.tvd12.ezyfox.codec.EzyEntityCodec;
import com.tvd12.ezyfox.util.EzyLoggable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@SuppressWarnings({"rawtypes", "unchecked"})
public class EntityBytesMapPersist extends EzyLoggable implements BytesMapPersist {

    protected final Class<?> keyType;
    protected final Class<?> valueType;
    protected final EntityMapPersist entityMapPersist;
    protected final EzyEntityCodec entityCodec;

    protected EntityBytesMapPersist(Builder builder) {
        this.entityMapPersist = builder.entityMapPersist;
        this.entityCodec = builder.entityCodec;
        this.keyType = entityMapPersist.getKeyType();
        this.valueType = entityMapPersist.getValueType();
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public Map<ByteArray, byte[]> loadAll() {
        Map entities = entityMapPersist.loadAll();
        return serializeEntities(entities);
    }

    @Override
    public Map<ByteArray, byte[]> load(Set<ByteArray> keys) {
        Map entities = entityMapPersist.load(keys);
        return serializeEntities(entities);
    }

    @Override
    public byte[] load(ByteArray key) {
        Object keyEntity = entityCodec.deserialize(key.getBytes(), keyType);
        Object valueEntity = entityMapPersist.load(keyEntity);
        if (valueEntity == null) {
            return null;
        }
        return entityCodec.serialize(valueEntity);
    }

    protected Map<ByteArray, byte[]> serializeEntities(Map entities) {
        Map<ByteArray, byte[]> answer = new HashMap<>();
        for (Object keyEntity : entities.keySet()) {
            Object valueEntity = entities.get(keyEntity);
            byte[] key = entityCodec.serialize(keyEntity);
            byte[] value = entityCodec.serialize(valueEntity);
            answer.put(new ByteArray(key), value);
        }
        return answer;
    }

    @Override
    public void persist(ByteArray key, byte[] value) {
        Object keyEntity = entityCodec.deserialize(key.getBytes(), keyType);
        Object valueEntity = entityCodec.deserialize(value, valueType);
        entityMapPersist.persist(keyEntity, valueEntity);
    }

    @Override
    public void persist(Map<ByteArray, byte[]> m) {
        Map entities = new HashMap<>();
        for (ByteArray key : m.keySet()) {
            byte[] value = m.get(key);
            Object keyEntity = entityCodec.deserialize(key.getBytes(), keyType);
            Object valueEntity = entityCodec.deserialize(value, valueType);
            entities.put(keyEntity, valueEntity);
        }
        entityMapPersist.persist(entities);
    }

    @Override
    public void delete(ByteArray key) {
        Object keyEntity = entityCodec.deserialize(key.getBytes(), keyType);
        entityMapPersist.delete(keyEntity);
    }

    @Override
    public void delete(Set<ByteArray> keys) {
        Set keyEntities = new HashSet<>();
        for (ByteArray key : keys) {
            Object keyEntity = entityCodec.deserialize(key.getBytes(), keyType);
            keyEntities.add(keyEntity);
        }
        entityMapPersist.delete(keyEntities);
    }

    public static class Builder implements EzyBuilder<EntityBytesMapPersist> {
        protected EntityMapPersist entityMapPersist;
        protected EzyEntityCodec entityCodec;

        public Builder entityMapPersist(EntityMapPersist entityMapPersist) {
            this.entityMapPersist = entityMapPersist;
            return this;
        }

        public Builder entityCodec(EzyEntityCodec entityCodec) {
            this.entityCodec = entityCodec;
            return this;
        }

        @Override
        public EntityBytesMapPersist build() {
            return new EntityBytesMapPersist(this);
        }
    }
}
