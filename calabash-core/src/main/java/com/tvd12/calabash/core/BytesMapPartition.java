package com.tvd12.calabash.core;

import com.tvd12.calabash.core.util.ByteArray;

import java.util.Map;
import java.util.Set;

public interface BytesMapPartition {

    void set(ByteArray key, byte[] value);

    byte[] put(ByteArray key, byte[] value);

    void putAll(Map<ByteArray, byte[]> m);

    byte[] get(ByteArray key);

    Map<ByteArray, byte[]> get(Set<ByteArray> keys);

    byte[] getByQuery(ByteArray key, byte[] query);

    byte[] remove(ByteArray key);

    void remove(Set<ByteArray> keys);

    long addAndGet(ByteArray key, long delta);

    int size();

    void clear();

    void evict();
}
