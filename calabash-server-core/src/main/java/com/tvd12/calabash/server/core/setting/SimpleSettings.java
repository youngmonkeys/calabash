package com.tvd12.calabash.server.core.setting;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

public class SimpleSettings implements Settings {

	@Setter
	@Getter
	protected int mapEvictionInterval = 3;
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
