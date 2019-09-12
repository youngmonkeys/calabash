package com.tvd12.calabash.backend.executor;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.tvd12.calabash.backend.BytesMapBackup;
import com.tvd12.calabash.backend.setting.MapBackupSetting;
import com.tvd12.calabash.backend.setting.MapSetting;
import com.tvd12.calabash.core.util.ByteArray;
import com.tvd12.ezyfox.util.EzyLoggable;

public class SimpleBytesMapBackupExecutor
		extends EzyLoggable
		implements BytesMapBackupExecutor {

	protected final Map<String, BytesMapBackup> mapBackups;
	
	public SimpleBytesMapBackupExecutor() {
		this.mapBackups = new HashMap<>();
	}
	
	@Override
	public void backup(MapSetting mapSetting, ByteArray key, byte[] value) {
		BytesMapBackup backup = mapBackups.get(mapSetting.getMapName());
		if(backup != null) {
			MapBackupSetting setting = mapSetting.getBackupSetting();
			if(!setting.isAsync())
				backup.backup(setting, key, value);
		}
	}

	@Override
	public void backup(MapSetting mapSetting, Map<ByteArray, byte[]> m) {
		BytesMapBackup backup = mapBackups.get(mapSetting.getMapName());
		if(backup != null) {
			MapBackupSetting setting = mapSetting.getBackupSetting();
			if(!setting.isAsync())
				backup.backup(setting, m);
		}
	}

	@Override
	public void remove(MapSetting mapSetting, ByteArray key) {
		BytesMapBackup backup = mapBackups.get(mapSetting.getMapName());
		if(backup != null) {
			MapBackupSetting setting = mapSetting.getBackupSetting();
			if(!setting.isAsync())
				backup.remove(setting, key);
		}
		
	}

	@Override
	public void remove(MapSetting mapSetting, Set<ByteArray> keys) {
		BytesMapBackup backup = mapBackups.get(mapSetting.getMapName());
		if(backup != null) {
			MapBackupSetting setting = mapSetting.getBackupSetting();
			if(!setting.isAsync())
				backup.remove(setting, keys);
		}
		
	}

	@Override
	public void clear(MapSetting mapSetting) {
		BytesMapBackup backup = mapBackups.get(mapSetting.getMapName());
		if(backup != null) {
			MapBackupSetting setting = mapSetting.getBackupSetting();
			if(!setting.isAsync())
				backup.clear(setting);
		}
	}
	
	public void addMapBackup(String mapName, BytesMapBackup backup) {
		this.mapBackups.put(mapName, backup);
	}

}
