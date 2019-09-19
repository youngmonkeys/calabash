package com.tvd12.calabash.local.setting;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

public class SimpleSettings implements Settings {

	@Getter
	@Setter
	protected int mapEvictionInterval = 3;
	protected Map<String, EntityMapSetting> mapSettings = new HashMap<>();
	
	public void addMapSetting(EntityMapSetting setting) {
		mapSettings.put(setting.getMapName(), setting);
	}
	
	@Override
	public EntityMapSetting getMapSetting(String mapName) {
		EntityMapSetting mapSetting = mapSettings.get(mapName);
		if(mapSetting == null) {
			mapSetting = newDefaultMapSetting(mapName);
		}
		return mapSetting;
	}
	
	protected EntityMapSetting newDefaultMapSetting(String mapName) {
		synchronized (mapSettings) {
			EntityMapSetting mapSetting = mapSettings.get(mapName);
			if (mapSetting == null) {
				mapSetting = new SimpleEntityMapSetting();
				mapSettings.put(mapName, mapSetting);
			}
			return mapSetting;
		}
	}
}
