package com.tvd12.calabash.server.core.executor;

import com.tvd12.calabash.core.util.ByteArray;
import com.tvd12.calabash.persist.BytesMapPersist;
import com.tvd12.calabash.persist.action.*;
import com.tvd12.calabash.persist.manager.MapPersistManager;
import com.tvd12.calabash.persist.setting.MapPersistSetting;
import com.tvd12.calabash.server.core.setting.MapSetting;
import com.tvd12.ezyfox.builder.EzyBuilder;
import com.tvd12.ezyfox.util.EzyLoggable;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class SimpleBytesMapPersistExecutor
    extends EzyLoggable
    implements BytesMapPersistExecutor {

    protected final MapPersistManager mapPersistManager;
    protected final PersistActionQueueManager actionQueueManager;

    public SimpleBytesMapPersistExecutor(Builder builder) {
        this.mapPersistManager = builder.mapPersistManager;
        this.actionQueueManager = builder.actionQueueManager;
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public Map<ByteArray, byte[]> loadAll(MapSetting mapSetting) {
        BytesMapPersist mapPersist = getMapPersist(mapSetting);
        if (mapPersist != null) {
            Map<ByteArray, byte[]> all = mapPersist.loadAll();
            return all;
        }
        return new HashMap<>();
    }

    @Override
    public Map<ByteArray, byte[]> load(MapSetting mapSetting, Set<ByteArray> keys) {
        BytesMapPersist mapPersist = getMapPersist(mapSetting);
        if (mapPersist != null) {
            Map<ByteArray, byte[]> all = mapPersist.load(keys);
            return all;
        }
        return new HashMap<>();
    }

    @Override
    public byte[] load(MapSetting mapSetting, ByteArray key) {
        BytesMapPersist mapPersist = getMapPersist(mapSetting);
        if (mapPersist != null) {
            byte[] value = mapPersist.load(key);
            return value;
        }
        return null;
    }

    @Override
    public byte[] loadByQuery(MapSetting mapSetting, ByteArray key, byte[] query) {
        BytesMapPersist mapPersist = getMapPersist(mapSetting);
        if (mapPersist != null) {
            byte[] value = mapPersist.loadByQuery(key, query);
            return value;
        }
        return null;
    }

    @Override
    public void persist(MapSetting mapSetting, ByteArray key, byte[] value) {
        BytesMapPersist mapPersist = getMapPersist(mapSetting);
        if (mapPersist != null) {
            PersistSaveOneAction action = new PersistSaveOneAction(key, value);
            addPersistActionToQueue(mapSetting, action);
        }
    }

    @Override
    public void persist(MapSetting mapSetting, Map<ByteArray, byte[]> m) {
        BytesMapPersist mapPersist = getMapPersist(mapSetting);
        if (mapPersist != null) {
            PersistSaveManyAction action = new PersistSaveManyAction(m);
            addPersistActionToQueue(mapSetting, action);
        }
    }

    @Override
    public void delete(MapSetting mapSetting, ByteArray key) {
        BytesMapPersist mapPersist = getMapPersist(mapSetting);
        if (mapPersist != null) {
            PersistDeleteOneAction action = new PersistDeleteOneAction(key);
            addPersistActionToQueue(mapSetting, action);
        }
    }

    @Override
    public void delete(MapSetting mapSetting, Set<ByteArray> keys) {
        BytesMapPersist mapPersist = getMapPersist(mapSetting);
        if (mapPersist != null) {
            PersistDeleteManyAction action = new PersistDeleteManyAction(keys);
            addPersistActionToQueue(mapSetting, action);
        }
    }

    @Override
    public boolean hasMapPersist(String mapName) {
        return mapPersistManager.hasMapPersist(mapName);
    }

    protected BytesMapPersist getMapPersist(MapSetting setting) {
        BytesMapPersist mp = mapPersistManager.getMapPersist(setting.getMapName());
        return mp;
    }

    protected void addPersistActionToQueue(MapSetting setting, PersistAction action) {
        String mapName = setting.getMapName();
        MapPersistSetting persistSetting = setting.getPersistSetting();
        long writeDelay = persistSetting.getWriteDelay();
        PersistActionQueue queue = writeDelay > 0
            ? actionQueueManager.getDelayedQueue(mapName)
            : actionQueueManager.getImmediateQueue(mapName);
        queue.add(action);
    }

    public static class Builder implements EzyBuilder<BytesMapPersistExecutor> {

        protected MapPersistManager mapPersistManager;
        protected PersistActionQueueManager actionQueueManager;

        public Builder mapPersistManager(MapPersistManager mapPersistManager) {
            this.mapPersistManager = mapPersistManager;
            return this;
        }

        public Builder actionQueueManager(PersistActionQueueManager actionQueueManager) {
            this.actionQueueManager = actionQueueManager;
            return this;
        }

        @Override
        public BytesMapPersistExecutor build() {
            return new SimpleBytesMapPersistExecutor(this);
        }
    }
}
