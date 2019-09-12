package com.tvd12.calabash.backend;

import java.util.Map;
import java.util.Set;

import com.tvd12.calabash.backend.setting.MapBackupSetting;
import com.tvd12.calabash.core.util.ByteArray;

public interface BytesMapBackup {

	void backup(MapBackupSetting setting, ByteArray key, byte[] value);

	void backup(MapBackupSetting setting, Map<ByteArray, byte[]> m);

	void remove(MapBackupSetting setting, ByteArray key);

	void remove(MapBackupSetting setting, Set<ByteArray> keys);

	void clear(MapBackupSetting setting);
	
}
