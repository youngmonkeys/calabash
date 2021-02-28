package com.tvd12.calabash.client.setting;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

public class SimpleSettings implements Settings {

	@Getter
	@Setter
	protected String atomicLongMapName;
	protected Map<String, EntityMapSetting> mapSettings;
	protected Map<String, MessageChannelSetting> channelSettings;
	
	public SimpleSettings() {
		this.mapSettings = new HashMap<>();
		this.channelSettings = new HashMap<>();
		this.atomicLongMapName = "___ezydata.atomic_longs___";
	}
	
	@Override
	public EntityMapSetting getMapSeting(String mapName) {
		EntityMapSetting setting = mapSettings.get(mapName);
		return setting;
	}
	
	@Override
	public MessageChannelSetting getChannelSeting(String channelName) {
		MessageChannelSetting setting = channelSettings.get(channelName);
		return setting;
	}
	
	public void addMapSettings(Map<String, EntityMapSetting> settings) {
		this.mapSettings.putAll(settings);
	}

	public void addMapSetting(String mapName, EntityMapSetting setting) {
		this.mapSettings.put(mapName, setting);
	}
	
	public void addChannelSettings(Map<String, MessageChannelSetting> settings) {
		this.channelSettings.putAll(settings);
	}

	public void addChannelSetting(String mapName, MessageChannelSetting setting) {
		this.channelSettings.put(mapName, setting);
	}
}
