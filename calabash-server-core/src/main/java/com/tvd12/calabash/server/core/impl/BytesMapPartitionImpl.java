package com.tvd12.calabash.server.core.impl;

import com.tvd12.calabash.converter.BytesLongConverter;
import com.tvd12.calabash.core.BytesMapPartition;
import com.tvd12.calabash.core.util.ByteArray;
import com.tvd12.calabash.eviction.MapEviction;
import com.tvd12.calabash.eviction.SimpleMapEviction;
import com.tvd12.calabash.server.core.executor.BytesMapBackupExecutor;
import com.tvd12.calabash.server.core.executor.BytesMapPersistExecutor;
import com.tvd12.calabash.server.core.setting.MapSetting;
import com.tvd12.ezyfox.builder.EzyBuilder;
import com.tvd12.ezyfox.concurrent.EzyConcurrentHashMapLockProvider;
import com.tvd12.ezyfox.concurrent.EzyMapLockProvider;
import com.tvd12.ezyfox.util.EzyLoggable;

import java.util.*;
import java.util.concurrent.locks.Lock;

public class BytesMapPartitionImpl
    extends EzyLoggable
    implements BytesMapPartition {

    protected final MapSetting mapSetting;
    protected final MapEviction mapEviction;
    protected final Map<ByteArray, byte[]> map;
    protected final EzyMapLockProvider lockProvider;
    protected final BytesLongConverter bytesLongConverter;
    protected final BytesMapBackupExecutor mapBackupExecutor;
    protected final BytesMapPersistExecutor mapPersistExecutor;

    public BytesMapPartitionImpl(Builder builder) {
        this.map = new HashMap<>();
        this.mapSetting = builder.mapSetting;
        this.bytesLongConverter = builder.bytesLongConverter;
        this.mapBackupExecutor = builder.mapBackupExecutor;
        this.mapPersistExecutor = builder.mapPersistExecutor;
        this.lockProvider = new EzyConcurrentHashMapLockProvider();
        this.mapEviction = newMapEviction();
    }

    public static Builder builder() {
        return new Builder();
    }

    protected MapEviction newMapEviction() {
        if (mapPersistExecutor.hasMapPersist(mapSetting.getMapName())) {
            return new SimpleMapEviction(mapSetting.getEvictionSetting());
        }
        return MapEviction.DEFAULT;
    }

    @Override
    public void set(ByteArray key, byte[] value) {
        put(key, value);
    }

    @Override
    public byte[] put(ByteArray key, byte[] value) {
        byte[] v;
        synchronized (map) {
            v = map.put(key, value);
            mapBackupExecutor.backup(mapSetting, key, value);
            mapPersistExecutor.persist(mapSetting, key, value);
        }
        mapEviction.updateKeyTime(key);
        return v;
    }

    @Override
    public void putAll(Map<ByteArray, byte[]> m) {
        synchronized (map) {
            map.putAll(m);
            mapBackupExecutor.backup(mapSetting, m);
            mapPersistExecutor.persist(mapSetting, m);
        }
        mapEviction.updateKeysTime(m.keySet());
    }

    @Override
    public byte[] get(ByteArray key) {
        byte[] value;
        synchronized (map) {
            value = map.get(key);
        }
        if (value == null) {
            value = load(key);
        }
        if (value != null) {
            mapEviction.updateKeyTime(key);
        }
        return value;

    }

    @Override
    public Map<ByteArray, byte[]> get(Set<ByteArray> keys) {
        Map<ByteArray, byte[]> answer = new HashMap<>();
        Set<ByteArray> unloadKeys = new HashSet<>();
        synchronized (map) {
            for (ByteArray key : keys) {
                byte[] value = map.get(key);
                if (value != null) {
                    answer.put(key, value);
                } else {
                    unloadKeys.add(key);
                }
            }
        }
        if (unloadKeys.size() > 0) {
            Map<ByteArray, byte[]> unloadItems = mapPersistExecutor.load(mapSetting, keys);
            synchronized (map) {
                for (ByteArray key : unloadItems.keySet()) {
                    byte[] value = map.get(key);
                    if (value == null) {
                        value = unloadItems.get(key);
                        map.put(key, value);
                    }
                    answer.put(key, value);
                }
            }
        }
        mapEviction.updateKeysTime(answer.keySet());
        return answer;
    }

    protected byte[] load(ByteArray key) {
        Lock keyLock = lockProvider.provideLock(key);
        byte[] unloadValue;
        keyLock.lock();
        try {
            byte[] value = map.get(key);
            if (value != null) {
                return value;
            }
            unloadValue = mapPersistExecutor.load(mapSetting, key);
        } finally {
            keyLock.unlock();
        }
        if (unloadValue != null) {
            synchronized (map) {
                map.putIfAbsent(key, unloadValue);
            }
        }
        return unloadValue;
    }

    @Override
    public byte[] getByQuery(ByteArray key, byte[] query) {
        byte[] value;
        synchronized (map) {
            value = map.get(key);
        }
        if (value == null) {
            value = loadByQuery(key, query);
        }
        if (value != null) {
            mapEviction.updateKeyTime(key);
        }
        return value;
    }

    protected byte[] loadByQuery(ByteArray key, byte[] query) {
        Lock lock = lockProvider.provideLock(key);
        byte[] unloadValue;
        lock.lock();
        try {
            byte[] value = map.get(key);
            if (value != null) {
                return value;
            }
            unloadValue = mapPersistExecutor.loadByQuery(mapSetting, key, query);
        } finally {
            lock.unlock();
        }
        if (unloadValue != null) {
            synchronized (map) {
                map.putIfAbsent(key, unloadValue);
            }
        }
        return unloadValue;
    }

    @Override
    public byte[] remove(ByteArray key) {
        byte[] v;
        synchronized (map) {
            v = map.remove(key);
            mapBackupExecutor.remove(mapSetting, key);
            mapPersistExecutor.delete(mapSetting, key);
        }
        if (v != null) {
            lockProvider.removeLock(key);
            mapEviction.removeKey(key);
        }
        return v;
    }

    @Override
    public void remove(Set<ByteArray> keys) {
        synchronized (map) {
            for (ByteArray key : keys) {
                map.remove(key);
            }
            mapBackupExecutor.remove(mapSetting, keys);
            mapPersistExecutor.delete(mapSetting, keys);
        }
        for (ByteArray key : keys) {
            lockProvider.removeLock(key);
        }
        mapEviction.removeKeys(keys);
    }

    @Override
    public long addAndGet(ByteArray key, long delta) {
        long nextValue = delta;
        synchronized (map) {
            byte[] valueBytes = map.get(key);
            if (valueBytes != null) {
                nextValue += bytesLongConverter.bytesToLong(valueBytes);
            }
            byte[] nextValueBytes = bytesLongConverter.longToBytes(nextValue);
            map.put(key, nextValueBytes);
            mapBackupExecutor.backup(mapSetting, key, nextValueBytes);
            mapPersistExecutor.persist(mapSetting, key, nextValueBytes);
        }
        mapEviction.updateKeyTime(key);
        return nextValue;
    }

    @Override
    public int size() {
        synchronized (map) {
            return map.size();
        }
    }

    @Override
    public void clear() {
        Set<ByteArray> keys;
        synchronized (map) {
            keys = new HashSet<>(map.keySet());
        }
        remove(keys);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void evict() {
        List<ByteArray> evictableKeys = mapEviction.getEvictableKeys();
        synchronized (map) {
            for (ByteArray key : evictableKeys) {
                map.remove(key);
            }
            mapBackupExecutor.remove(mapSetting, evictableKeys);
        }
        for (ByteArray key : evictableKeys) {
            lockProvider.removeLock(key);
        }
    }

    public static class Builder implements EzyBuilder<BytesMapPartition> {

        protected MapSetting mapSetting;
        protected BytesLongConverter bytesLongConverter;
        protected BytesMapBackupExecutor mapBackupExecutor;
        protected BytesMapPersistExecutor mapPersistExecutor;

        public Builder mapSetting(MapSetting mapSetting) {
            this.mapSetting = mapSetting;
            return this;
        }

        public Builder bytesLongConverter(BytesLongConverter bytesLongConverter) {
            this.bytesLongConverter = bytesLongConverter;
            return this;
        }

        public Builder mapBackupExecutor(BytesMapBackupExecutor mapBackupExecutor) {
            this.mapBackupExecutor = mapBackupExecutor;
            return this;
        }

        public Builder mapPersistExecutor(BytesMapPersistExecutor mapPersistExecutor) {
            this.mapPersistExecutor = mapPersistExecutor;
            return this;
        }

        @Override
        public BytesMapPartition build() {
            return new BytesMapPartitionImpl(this);
        }

    }

}
