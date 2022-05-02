package com.tvd12.calabash.core;

import com.tvd12.calabash.core.util.ByteArray;

import java.util.*;
import java.util.Map.Entry;

public interface BytesMap extends IMap {

    Map<ByteArray, byte[]> loadAll();

    void set(ByteArray key, byte[] value);

    default void set(byte[] key, byte[] value) {
        set(ByteArray.wrap(key), value);
    }

    byte[] put(ByteArray key, byte[] value);

    default byte[] put(byte[] key, byte[] value) {
        return put(ByteArray.wrap(key), value);
    }

    void putAll(Map<ByteArray, byte[]> m);

    byte[] get(ByteArray key);

    default byte[] get(byte[] key) {
        return get(ByteArray.wrap(key));
    }

    Map<ByteArray, byte[]> get(Collection<ByteArray> keys);

    byte[] getByQuery(ByteArray key, byte[] query);

    default byte[] getByQuery(byte[] key, byte[] query) {
        return getByQuery(ByteArray.wrap(key), query);
    }

    byte[] remove(ByteArray key);

    default byte[] remove(byte[] key) {
        return remove(ByteArray.wrap(key));
    }

    default void remove(byte[][] keys) {
        Set<ByteArray> tmp = new HashSet<>(keys.length);
        for (byte[] key : keys) {
            tmp.add(ByteArray.wrap(key));
        }
        remove(tmp);
    }

    void remove(Set<ByteArray> keys);

    long addAndGet(ByteArray key, long delta);

    default long addAndGet(String key, long delta) {
        return addAndGet(ByteArray.wrap(key), delta);
    }

    default void setAll(Map<byte[], byte[]> m) {
        Map<ByteArray, byte[]> tmp = new HashMap<>(m.size());
        for (Entry<byte[], byte[]> e : m.entrySet()) {
            tmp.put(ByteArray.wrap(e.getKey()), e.getValue());
        }
        putAll(tmp);
    }

    default boolean containsKey(byte[] key) {
        return get(key) != null;
    }
}
