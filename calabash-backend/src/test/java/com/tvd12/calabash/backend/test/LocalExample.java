package com.tvd12.calabash.backend.test;

import com.tvd12.calabash.backend.builder.BytesMapBuilder;
import com.tvd12.calabash.backend.executor.BytesMapBackupExecutor;
import com.tvd12.calabash.backend.executor.BytesMapPersistExecutor;
import com.tvd12.calabash.backend.executor.SimpleBytesMapBackupExecutor;
import com.tvd12.calabash.backend.executor.SimpleBytesMapPersistExecutor;
import com.tvd12.calabash.backend.setting.Settings;
import com.tvd12.calabash.backend.setting.SimpleSettings;
import com.tvd12.calabash.core.BytesMap;
import com.tvd12.calabash.core.util.ByteArray;

public class LocalExample {

	public static void main(String[] args) {
		Settings settings = new SimpleSettings();
		BytesMapBackupExecutor backupExecutor = new SimpleBytesMapBackupExecutor();
		BytesMapPersistExecutor persistExecutor = new SimpleBytesMapPersistExecutor();
		BytesMap bytesMap = new BytesMapBuilder()
				.mapSetting(settings.getMapSetting("test"))
				.mapBackupExecutor(backupExecutor)
				.mapPersistExecutor(persistExecutor)
				.build();
		bytesMap.loadAll();
		bytesMap.put(new ByteArray(new byte[] {1, 2, 3}), new byte[] {1, 2, 3});
		System.out.println(bytesMap.get(new ByteArray(new byte[] {1, 2, 3})));
	}
	
}