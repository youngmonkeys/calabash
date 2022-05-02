package com.tvd12.calabash.server.core.executor;

import com.tvd12.calabash.core.util.ByteArray;
import com.tvd12.calabash.server.core.setting.MapSetting;

import java.util.Map;
import java.util.Set;

public interface BytesMapPersistExecutor {

    Map<ByteArray, byte[]> loadAll(MapSetting mapSetting);

    Map<ByteArray, byte[]> load(MapSetting mapSetting, Set<ByteArray> keys);

    byte[] load(MapSetting mapSetting, ByteArray key);

    byte[] loadByQuery(MapSetting mapSetting, ByteArray key, byte[] query);

    void persist(MapSetting mapSetting, ByteArray key, byte[] value);

    void persist(MapSetting mapSetting, Map<ByteArray, byte[]> m);

    void delete(MapSetting mapSetting, ByteArray key);

    void delete(MapSetting mapSetting, Set<ByteArray> keys);

    boolean hasMapPersist(String mapName);
}
