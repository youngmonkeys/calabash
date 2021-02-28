package com.tvd12.calabash.client.setting;

import com.tvd12.ezyfox.builder.EzyBuilder;
import com.tvd12.ezyfox.reflect.EzyClasses;

public class EntityMapSettingBuilder implements EzyBuilder<EntityMapSetting> {

	protected Class<?> keyType;
	protected Class<?> valueType;
	protected SettingsBuilder parent;
	
	public EntityMapSettingBuilder() {}
	
	public EntityMapSettingBuilder(SettingsBuilder parent) {
		this.parent = parent;
	}
	
	public EntityMapSettingBuilder keyType(Class<?> keyType) {
		if(keyType != null)
			this.keyType = keyType;
		return this;
	}
	
	public EntityMapSettingBuilder keyType(String keyType) {
		if(keyType != null)
			this.keyType = EzyClasses.getClass(keyType);
		return this;
	}
	
	public EntityMapSettingBuilder valueType(Class<?> valueType) {
		if(valueType != null)
			this.valueType = valueType;
		return this;
	}
	
	public EntityMapSettingBuilder valueType(String valueType) {
		if(valueType != null)
			this.valueType = EzyClasses.getClass(valueType);
		return this;
	}
	
	public SettingsBuilder parent() {
		return parent;
	}
	
	@Override
	public EntityMapSetting build() {
		SimpleMapSetting setting = new SimpleMapSetting();
		setting.setKeyType(keyType);
		setting.setValueType(valueType);
		return setting;
	}


}
