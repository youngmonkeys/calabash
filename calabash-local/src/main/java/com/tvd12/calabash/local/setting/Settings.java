package com.tvd12.calabash.local.setting;

import com.tvd12.calabash.core.setting.IMapSettings;

public interface Settings extends IMapSettings {

	EntityMapSetting getMapSetting(String mapName);
	
}
