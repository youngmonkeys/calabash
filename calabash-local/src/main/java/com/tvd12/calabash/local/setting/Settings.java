package com.tvd12.calabash.local.setting;

import com.tvd12.calabash.core.setting.IMapSettings;
import com.tvd12.calabash.core.setting.ISettings;

public interface Settings extends ISettings, IMapSettings {

	EntityMapSetting getMapSetting(String mapName);
	
}
