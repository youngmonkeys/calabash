package com.tvd12.calabash.local.executor;

import com.tvd12.calabash.local.setting.EntityMapSetting;

import java.util.Map;
import java.util.Set;

@SuppressWarnings("rawtypes")
public interface EntityMapPersistExecutor {

    Map loadAll(EntityMapSetting mapSetting);

    Map load(EntityMapSetting mapSetting, Set keys);

    Object load(EntityMapSetting mapSetting, Object key);

    Object loadByQuery(EntityMapSetting mapSetting, Object query);

    void persist(EntityMapSetting mapSetting, Object key, Object value);

    void persist(EntityMapSetting mapSetting, Map m);

    void delete(EntityMapSetting mapSetting, Object key);

    void delete(EntityMapSetting mapSetting, Set keys);

    boolean hasMapPersist(String mapName);
}
