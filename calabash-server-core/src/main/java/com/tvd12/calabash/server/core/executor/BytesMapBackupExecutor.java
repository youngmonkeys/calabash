package com.tvd12.calabash.server.core.executor;

import com.tvd12.calabash.core.util.ByteArray;
import com.tvd12.calabash.server.core.setting.MapSetting;

import java.util.Collection;
import java.util.Map;

public interface BytesMapBackupExecutor {

    void backup(MapSetting mapSetting, ByteArray key, byte[] value);

    void backup(MapSetting mapSetting, Map<ByteArray, byte[]> m);

    void remove(MapSetting mapSetting, ByteArray key);

    void remove(MapSetting mapSetting, Collection<ByteArray> keys);

    void clear(MapSetting mapSetting);
}
