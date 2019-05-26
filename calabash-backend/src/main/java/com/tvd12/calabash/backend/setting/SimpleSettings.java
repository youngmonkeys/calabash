package com.tvd12.calabash.backend.setting;

import java.util.HashMap;
import java.util.Map;

public class SimpleSettings implements Settings {

	protected final Object synchronizedLock = new Object();
	protected Map<String, MapSetting> mapSettings = new HashMap<>();
	
	@Override
	public MapSetting getMapSetting(String mapName) {
		MapSetting mapSetting = mapSettings.get(mapName);
		if(mapSetting == null) {
			mapSetting = newDefaultMapSetting(mapName);
		}
		return mapSetting;
	}
	
	protected MapSetting newDefaultMapSetting(String mapName) {
		synchronized (synchronizedLock) {
			MapSetting mapSetting = mapSettings.get(mapName);
			if (mapSetting == null) {
				mapSetting = SimpleMapSetting.defaultSetting(mapName);
				mapSettings.put(mapName, mapSetting);
			}
			return mapSetting;
		}
	}
}
