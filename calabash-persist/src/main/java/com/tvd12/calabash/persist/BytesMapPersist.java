package com.tvd12.calabash.persist;

import com.tvd12.calabash.core.util.ByteArray;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public interface BytesMapPersist {

    byte[] load(ByteArray key);

    default Map<ByteArray, byte[]> load(Set<ByteArray> keys) {
        Map<ByteArray, byte[]> keyValues = new HashMap<>();
        for (ByteArray key : keys) {
            byte[] value = load(key);
            keyValues.put(key, value);
        }
        return keyValues;
    }

    void persist(ByteArray key, byte[] value);

    default void persist(Map<ByteArray, byte[]> m) {
        for (ByteArray key : m.keySet()) {
            byte[] value = m.get(key);
            persist(key, value);
        }
    }

    default byte[] loadByQuery(ByteArray key, byte[] query) {
        return null;
    }

    default void delete(ByteArray key) {}

    default void delete(Set<ByteArray> keys) {
        for (ByteArray key : keys) {
            delete(key);
        }
    }

    default Map<ByteArray, byte[]> loadAll() {
        return new HashMap<>();
    }
}
