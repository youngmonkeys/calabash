package com.tvd12.calabash.server.core.setting;

public interface MapSetting {

	String getMapName();
	
	MapBackupSetting getBackupSetting();
	
	MapPersistSetting getPersistSetting();
	
}
