package com.tvd12.calabash.client.setting;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.tvd12.ezyfox.builder.EzyBuilder;
import com.tvd12.properties.file.util.PropertiesUtil;

public class SettingsBuilder implements EzyBuilder<Settings> {

	protected String atomicLongMapName;
	protected Map<String, EntityMapSetting> mapSettings;
	protected Map<String, MessageChannelSetting> channelSettings;
	protected Map<String, EntityMapSettingBuilder> mapSettingBuilders;
	protected Map<String, MessageChannelSettingBuilder> channelSettingBuilders;
	
	public SettingsBuilder() {
		this.mapSettings = new HashMap<>();
		this.mapSettingBuilders = new HashMap<>();
		this.channelSettings = new HashMap<>();
		this.channelSettingBuilders = new HashMap<>();
		this.atomicLongMapName = "___ezydata.atomic_longs___";
	}
	
	public SettingsBuilder atomicLongMapName(String atomicLongMapName) {
		if(atomicLongMapName != null)
			this.atomicLongMapName = atomicLongMapName;
		return this;
	}
	
	public SettingsBuilder addMapSetting(String mapName, EntityMapSetting setting) {
		this.mapSettings.compute(mapName, (k, v) -> {
			if(v == null)
				return setting;
			return new EntityMapSettingBuilder()
					.keyType(setting.getKeyType())
					.valueType(setting.getValueType())
					.build();
		});
		return this;
	}
	
	public EntityMapSettingBuilder mapSettingBuilder(String mapName) {
		EntityMapSettingBuilder builder = mapSettingBuilders.get(mapName);
		if(builder == null) {
			builder = new EntityMapSettingBuilder(this);
			mapSettingBuilders.put(mapName, builder);
		}
		return builder;
	}
	
	public SettingsBuilder addChannelSetting(String channelName, MessageChannelSetting setting) {
		this.channelSettings.compute(channelName, (k, v) -> {
			if(v == null)
				return setting;
			return new MessageChannelSettingBuilder()
					.messageType(setting.getMessageType())
					.subThreadPoolSize(setting.getSubThreadPoolSize())
					.build();
		});
		return this;
	}
	
	public MessageChannelSettingBuilder channelSettingBuilder(String channelName) {
		MessageChannelSettingBuilder builder = channelSettingBuilders.get(channelName);
		if(builder == null) {
			builder = new MessageChannelSettingBuilder(this);
			channelSettingBuilders.put(channelName, builder);
		}
		return builder;
	}
	
	public SettingsBuilder properties(Properties properties) {
		atomicLongMapName(properties.getProperty(Settings.ATOMIC_LONG_MAP_NAME));
		Map<String, Properties> mapsProperties = 
				PropertiesUtil.getPropertiesMap(
						PropertiesUtil.getPropertiesByPrefix(properties, Settings.MAPS));
		for(String mapName : mapsProperties.keySet()) {
			Properties mapProperties = mapsProperties.get(mapName);	
			mapSettingBuilder((String)mapName)
				.keyType(mapProperties.getProperty(Settings.MAP_KEY_TYPE))
				.valueType(mapProperties.getProperty(Settings.MAP_VALUE_TYPE));
		}
		Map<String, Properties> channelsProperties = 
				PropertiesUtil.getPropertiesMap(
						PropertiesUtil.getPropertiesByPrefix(properties, Settings.CHANNELS));
		for(String channelName : channelsProperties.keySet()) {
			Properties channelProperties = channelsProperties.get(channelName);
			channelSettingBuilder((String)channelName)
				.messageType(channelProperties.getProperty(Settings.CHANNEL_MESSAGE_TYPE))
				.subThreadPoolSize(channelProperties.getProperty(Settings.CHANNEL_THREAD_POOL_SIZE));
		}
		return this;
	}
	
	@Override
	public Settings build() {
		buildMapSettings();
		buildChannelSettings();
		SimpleSettings settings = new SimpleSettings();
		settings.addMapSettings(mapSettings);
		settings.addChannelSettings(channelSettings);
		settings.setAtomicLongMapName(atomicLongMapName);
		return settings;
	}
	
	protected void buildMapSettings() {
		for(String mapName : mapSettingBuilders.keySet()) {
			EntityMapSettingBuilder b = mapSettingBuilders.get(mapName);
			addMapSetting(mapName, b.build());
		}
	}
	
	protected void buildChannelSettings() {
		for(String channelName : channelSettingBuilders.keySet()) {
			MessageChannelSettingBuilder b = channelSettingBuilders.get(channelName);
			addChannelSetting(channelName, b.build());
		}
	}
	
}
