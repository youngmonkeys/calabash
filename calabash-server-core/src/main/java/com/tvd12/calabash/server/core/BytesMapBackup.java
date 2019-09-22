package com.tvd12.calabash.server.core;

import java.util.Collection;
import java.util.Map;

import com.tvd12.calabash.core.util.ByteArray;
import com.tvd12.calabash.server.core.setting.MapBackupSetting;

public interface BytesMapBackup {

	void backup(MapBackupSetting setting, ByteArray key, byte[] value);

	void backup(MapBackupSetting setting, Map<ByteArray, byte[]> m);

	void remove(MapBackupSetting setting, ByteArray key);

	void remove(MapBackupSetting setting, Collection<ByteArray> keys);

	void clear(MapBackupSetting setting);
	
}
