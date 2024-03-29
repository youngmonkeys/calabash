package com.tvd12.calabash.local.impl;

import com.tvd12.calabash.core.EntityMapPartition;
import com.tvd12.calabash.eviction.MapEviction;
import com.tvd12.calabash.eviction.SimpleMapEviction;
import com.tvd12.calabash.local.executor.EntityMapPersistExecutor;
import com.tvd12.calabash.local.setting.EntityMapSetting;
import com.tvd12.ezyfox.builder.EzyBuilder;
import com.tvd12.ezyfox.concurrent.EzyConcurrentHashMapLockProvider;
import com.tvd12.ezyfox.concurrent.EzyMapLockProvider;
import com.tvd12.ezyfox.function.EzyVoid;
import com.tvd12.ezyfox.util.EzyLoggable;

import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.locks.Lock;

@SuppressWarnings({"rawtypes", "unchecked"})
public class EntityMapPartitionImpl<K, V>
    extends EzyLoggable
    implements EntityMapPartition<K, V> {

    protected final Map<K, V> map;
    protected final MapEviction mapEviction;
    protected final EntityMapSetting mapSetting;
    protected final EzyMapLockProvider lockProvider;
    protected final EntityMapPersistExecutor mapPersistExecutor;

    protected static final EzyVoid EMPTY_FUNC = () -> {};

    public EntityMapPartitionImpl(Builder builder) {
        this.map = new HashMap<>();
        this.mapSetting = builder.mapSetting;
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
    public void set(K key, V value) {
        put(key, value);
    }

    @Override
    public V put(K key, V value) {
        V v;
        synchronized (map) {
            v = map.put(key, value);
            mapPersistExecutor.persist(mapSetting, key, value);
        }
        mapEviction.updateKeyTime(key);
        return v;
    }

    @Override
    public void putAll(Map<K, V> m) {
        synchronized (map) {
            map.putAll(m);
            mapPersistExecutor.persist(mapSetting, m);
        }
        mapEviction.updateKeysTime(m.keySet());
    }

    @Override
    public V get(Object key) {
        V value;
        synchronized (map) {
            //noinspection SuspiciousMethodCalls
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
    public Map<K, V> get(Set<K> keys) {
        Map<K, V> answer = new HashMap<>();
        Set<K> unloadKeys = new HashSet<>();
        synchronized (map) {
            for (K key : keys) {
                V value = map.get(key);
                if (value != null) {
                    answer.put(key, value);
                } else {
                    unloadKeys.add(key);
                }
            }
        }
        if (unloadKeys.size() > 0) {
            Map<K, V> unloadItems = mapPersistExecutor.load(mapSetting, unloadKeys);
            synchronized (map) {
                for (K key : unloadItems.keySet()) {
                    V value = map.get(key);
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

    protected V load(Object key) {
        Lock lock = lockProvider.provideLock(key);
        V unloadValue;
        lock.lock();
        try {
            //noinspection SuspiciousMethodCalls
            V value = map.get(key);
            if (value != null) {
                return value;
            }
            unloadValue = (V) mapPersistExecutor.load(mapSetting, key);
        } finally {
            lock.unlock();
        }
        if (unloadValue != null) {
            synchronized (map) {
                map.putIfAbsent((K) key, unloadValue);
            }
        }
        return unloadValue;
    }

    @Override
    public V getByQuery(K key, Object query) {
        V value;
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

    protected V loadByQuery(K key, Object query) {
        Lock lock = lockProvider.provideLock(key);
        V unloadValue;
        lock.lock();
        try {
            V value = map.get(key);
            if (value != null) {
                return value;
            }
            unloadValue = (V) mapPersistExecutor.loadByQuery(mapSetting, query);
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
    public V remove(Object key) {
        V v;
        synchronized (map) {
            //noinspection SuspiciousMethodCalls
            v = map.remove(key);
            if (v != null) {
                mapPersistExecutor.delete(mapSetting, key);
            }
        }
        if (v != null) {
            lockProvider.removeLock(key);
            mapEviction.removeKey(key);
        }
        return v;
    }

    @Override
    public void remove(Set<K> keys) {
        remove(keys, () -> mapPersistExecutor.delete(mapSetting, keys));
        mapEviction.removeKeys(keys);
    }

    protected void remove(Collection<K> keys, EzyVoid postRemoveFunc) {
        synchronized (map) {
            for (K key : keys) {
                map.remove(key);
            }
            postRemoveFunc.apply();
        }
        for (K key : keys) {
            lockProvider.removeLock(key);
        }
    }

    @Override
    public int size() {
        synchronized (map) {
            return map.size();
        }
    }

    @Override
    public void clear() {
        Set<K> keys;
        synchronized (map) {
            keys = new HashSet<>(map.keySet());
        }
        remove(keys);
    }

    @Override
    public void evict() {
        List<K> evictableKeys = mapEviction.getEvictableKeys();
        remove(evictableKeys, EMPTY_FUNC);
    }

    @Override
    public boolean containsValue(Object value) {
        synchronized (map) {
            //noinspection SuspiciousMethodCalls
            return map.containsValue(value);
        }
    }

    @Override
    public Set<K> keySet() {
        synchronized (map) {
            return map.keySet();
        }
    }

    @Override
    public Collection<V> values() {
        synchronized (map) {
            return map.values();
        }
    }

    @Override
    public Collection<Entry<K, V>> entrySet() {
        synchronized (map) {
            return map.entrySet();
        }
    }

    public static class Builder implements EzyBuilder<EntityMapPartition> {

        protected EntityMapSetting mapSetting;
        protected EntityMapPersistExecutor mapPersistExecutor;

        public Builder mapSetting(EntityMapSetting mapSetting) {
            this.mapSetting = mapSetting;
            return this;
        }

        public Builder mapPersistExecutor(EntityMapPersistExecutor mapPersistExecutor) {
            this.mapPersistExecutor = mapPersistExecutor;
            return this;
        }

        @Override
        public EntityMapPartition build() {
            return new EntityMapPartitionImpl(this);
        }

    }

}
