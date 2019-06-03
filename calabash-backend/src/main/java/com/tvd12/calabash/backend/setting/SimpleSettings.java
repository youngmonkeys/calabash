package com.tvd12.calabash.backend.setting;

import java.util.HashMap;
import java.util.Map;

public class SimpleSettings implements Settings {

	protected Map<String, MapSetting> mapSettings = new HashMap<>();
	
	public void addMapSetting(MapSetting setting) {
		mapSettings.put(setting.getMapName(), setting);
	}
	
	@Override
	public MapSetting getMapSetting(String mapName) {
		MapSetting mapSetting = mapSettings.get(mapName);
		if(mapSetting == null) {
			mapSetting = newDefaultMapSetting(mapName);
		}
		return mapSetting;
	}
	
	protected MapSetting newDefaultMapSetting(String mapName) {
		synchronized (mapSettings) {
			MapSetting mapSetting = mapSettings.get(mapName);
			if (mapSetting == null) {
				mapSetting = new SimpleMapSetting();
				mapSettings.put(mapName, mapSetting);
			}
			return mapSetting;
		}
	}
}
