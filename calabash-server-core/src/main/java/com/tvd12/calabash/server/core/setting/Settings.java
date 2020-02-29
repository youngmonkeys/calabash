package com.tvd12.calabash.server.core.setting;

import com.tvd12.calabash.core.setting.IMapSettings;
import com.tvd12.calabash.core.setting.ISettings;

public interface Settings extends ISettings, IMapSettings {

	MapSetting getMapSetting(String mapName);
	
}
