package com.tvd12.calabash.server.core.setting;

import com.tvd12.calabash.core.setting.IMapSetting;

public interface MapSetting extends IMapSetting {

	MapBackupSetting getBackupSetting();
	
	@Override
	MapPersistSetting getPersistSetting();
	
	@Override
	MapEvictionSetting getEvictionSetting();
	
}
