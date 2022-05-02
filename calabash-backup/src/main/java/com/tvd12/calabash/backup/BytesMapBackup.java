package com.tvd12.calabash.backup;

import com.tvd12.calabash.backup.setting.MapBackupSetting;
import com.tvd12.calabash.core.util.ByteArray;

import java.util.Collection;
import java.util.Map;

public interface BytesMapBackup {

    void backup(MapBackupSetting setting, ByteArray key, byte[] value);

    void backup(MapBackupSetting setting, Map<ByteArray, byte[]> m);

    void remove(MapBackupSetting setting, ByteArray key);

    void remove(MapBackupSetting setting, Collection<ByteArray> keys);

    void clear(MapBackupSetting setting);
}
