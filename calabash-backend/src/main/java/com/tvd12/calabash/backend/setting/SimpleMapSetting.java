package com.tvd12.calabash.backend.setting;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SimpleMapSetting implements MapSetting {

	protected String mapName;
	protected SimpleMapBackupSetting backupSetting;
	protected SimpleMapPersistSetting persistSetting;
	
	public static SimpleMapSetting defaultSetting(String name) {
		SimpleMapSetting setting = new SimpleMapSetting();
		setting.mapName = name;
		setting.backupSetting = new SimpleMapBackupSetting();
		setting.persistSetting = new SimpleMapPersistSetting();
		return setting;
		
	}
	
}
