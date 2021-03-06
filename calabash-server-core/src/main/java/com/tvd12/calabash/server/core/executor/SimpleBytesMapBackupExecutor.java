package com.tvd12.calabash.server.core.executor;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.tvd12.calabash.backup.BytesMapBackup;
import com.tvd12.calabash.backup.setting.MapBackupSetting;
import com.tvd12.calabash.core.util.ByteArray;
import com.tvd12.calabash.server.core.setting.MapSetting;
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
	public void remove(MapSetting mapSetting, Collection<ByteArray> keys) {
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
