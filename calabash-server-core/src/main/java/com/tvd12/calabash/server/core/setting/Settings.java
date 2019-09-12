package com.tvd12.calabash.server.core.setting;

import com.tvd12.calabash.core.setting.IMapSettings;

public interface Settings extends IMapSettings {

	MapSetting getMapSetting(String mapName);
	
}
