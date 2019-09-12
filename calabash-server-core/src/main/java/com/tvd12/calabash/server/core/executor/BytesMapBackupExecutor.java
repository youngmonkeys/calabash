package com.tvd12.calabash.server.core.executor;

import java.util.Map;
import java.util.Set;

import com.tvd12.calabash.core.util.ByteArray;
import com.tvd12.calabash.server.core.setting.MapSetting;

public interface BytesMapBackupExecutor {

	void backup(MapSetting mapSetting, ByteArray key, byte[] value);
	
	void backup(MapSetting mapSetting, Map<ByteArray, byte[]> m);
	
	void remove(MapSetting mapSetting, ByteArray key);
	
	void remove(MapSetting mapSetting, Set<ByteArray> keys);
	
	void clear(MapSetting mapSetting);
	
}
