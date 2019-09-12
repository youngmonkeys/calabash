package com.tvd12.calabash.backend.setting;

public interface MapSetting {

	String getMapName();
	
	MapBackupSetting getBackupSetting();
	
	MapPersistSetting getPersistSetting();
	
}
