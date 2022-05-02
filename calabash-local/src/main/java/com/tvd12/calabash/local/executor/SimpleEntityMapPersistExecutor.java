package com.tvd12.calabash.local.executor;

import com.tvd12.calabash.local.setting.EntityMapPersistSetting;
import com.tvd12.calabash.local.setting.EntityMapSetting;
import com.tvd12.calabash.persist.EntityMapPersist;
import com.tvd12.calabash.persist.action.*;
import com.tvd12.calabash.persist.manager.MapPersistManager;
import com.tvd12.ezyfox.builder.EzyBuilder;
import com.tvd12.ezyfox.util.EzyLoggable;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@SuppressWarnings({"rawtypes", "unchecked"})
public class SimpleEntityMapPersistExecutor
    extends EzyLoggable
    implements EntityMapPersistExecutor {

    protected final MapPersistManager mapPersistManager;
    protected final PersistActionQueueManager actionQueueManager;

    public SimpleEntityMapPersistExecutor(Builder builder) {
        this.mapPersistManager = builder.mapPersistManager;
        this.actionQueueManager = builder.actionQueueManager;
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public Map loadAll(EntityMapSetting mapSetting) {
        EntityMapPersist mapPersist = getMapPersist(mapSetting);
        if (mapPersist != null) {
            return mapPersist.loadAll();
        }
        return new HashMap<>();
    }

    @Override
    public Map load(EntityMapSetting mapSetting, Set keys) {
        EntityMapPersist mapPersist = getMapPersist(mapSetting);
        if (mapPersist != null) {
            return mapPersist.load(keys);
        }
        return new HashMap<>();
    }

    @Override
    public Object load(EntityMapSetting mapSetting, Object key) {
        EntityMapPersist mapPersist = getMapPersist(mapSetting);
        if (mapPersist != null) {
            return mapPersist.load(key);
        }
        return null;
    }

    @Override
    public Object loadByQuery(EntityMapSetting mapSetting, Object query) {
        EntityMapPersist mapPersist = getMapPersist(mapSetting);
        if (mapPersist != null) {
            return mapPersist.loadByQuery(query);
        }
        return null;
    }

    @Override
    public void persist(EntityMapSetting mapSetting, Object key, Object value) {
        EntityMapPersist mapPersist = getMapPersist(mapSetting);
        if (mapPersist != null) {
            PersistSaveOneAction action = new PersistSaveOneAction(key, value);
            addPersistActionToQueue(mapSetting, action);
        }
    }

    @Override
    public void persist(EntityMapSetting mapSetting, Map m) {
        EntityMapPersist mapPersist = getMapPersist(mapSetting);
        if (mapPersist != null) {
            PersistSaveManyAction action = new PersistSaveManyAction(m);
            addPersistActionToQueue(mapSetting, action);
        }
    }

    @Override
    public void delete(EntityMapSetting mapSetting, Object key) {
        EntityMapPersist mapPersist = getMapPersist(mapSetting);
        if (mapPersist != null) {
            PersistDeleteOneAction action = new PersistDeleteOneAction(key);
            addPersistActionToQueue(mapSetting, action);
        }
    }

    @Override
    public void delete(EntityMapSetting mapSetting, Set keys) {
        EntityMapPersist mapPersist = getMapPersist(mapSetting);
        if (mapPersist != null) {
            PersistDeleteManyAction action = new PersistDeleteManyAction(keys);
            addPersistActionToQueue(mapSetting, action);
        }
    }

    @Override
    public boolean hasMapPersist(String mapName) {
        return mapPersistManager.hasMapPersist(mapName);
    }

    protected EntityMapPersist getMapPersist(EntityMapSetting setting) {
        return mapPersistManager.getMapPersist(setting.getMapName());
    }

    protected void addPersistActionToQueue(EntityMapSetting setting, PersistAction action) {
        String mapName = setting.getMapName();
        EntityMapPersistSetting persistSetting = setting.getPersistSetting();
        long writeDelay = persistSetting.getWriteDelay();
        PersistActionQueue queue = writeDelay > 0
            ? actionQueueManager.getDelayedQueue(mapName)
            : actionQueueManager.getImmediateQueue(mapName);
        queue.add(action);
    }

    public static class Builder implements EzyBuilder<EntityMapPersistExecutor> {

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
        public EntityMapPersistExecutor build() {
            return new SimpleEntityMapPersistExecutor(this);
        }
    }
}
