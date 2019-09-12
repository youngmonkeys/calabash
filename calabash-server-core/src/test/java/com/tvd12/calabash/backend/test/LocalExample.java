package com.tvd12.calabash.backend.test;

import com.tvd12.calabash.core.BytesMap;
import com.tvd12.calabash.core.util.ByteArray;
import com.tvd12.calabash.persist.manager.MapPersistManager;
import com.tvd12.calabash.persist.manager.SimpleMapPersistManager;
import com.tvd12.calabash.server.core.builder.BytesMapBuilder;
import com.tvd12.calabash.server.core.executor.BytesMapBackupExecutor;
import com.tvd12.calabash.server.core.executor.BytesMapPersistExecutor;
import com.tvd12.calabash.server.core.executor.SimpleBytesMapBackupExecutor;
import com.tvd12.calabash.server.core.executor.SimpleBytesMapPersistExecutor;
import com.tvd12.calabash.server.core.setting.Settings;
import com.tvd12.calabash.server.core.setting.SimpleSettings;

public class LocalExample {

	public static void main(String[] args) {
		Settings settings = new SimpleSettings();
		MapPersistManager mapPersistManager = new SimpleMapPersistManager();
		BytesMapBackupExecutor backupExecutor = new SimpleBytesMapBackupExecutor();
		BytesMapPersistExecutor persistExecutor = SimpleBytesMapPersistExecutor.builder()
				.mapPersistManager(mapPersistManager)
				.build();
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
