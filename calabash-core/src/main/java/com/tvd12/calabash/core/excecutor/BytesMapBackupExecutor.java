package com.tvd12.calabash.core.excecutor;

import java.util.Map;
import java.util.Set;

import com.tvd12.calabash.core.util.ByteArray;

public interface BytesMapBackupExecutor {

	void backup(String mapName, ByteArray key, byte[] value);
	
	void backup(String mapName, Map<ByteArray, byte[]> m);
	
	void remove(String mapName, ByteArray key);
	
	void remove(String mapName, Set<ByteArray> keys);
	
	void clear(String mapName);
	
}
