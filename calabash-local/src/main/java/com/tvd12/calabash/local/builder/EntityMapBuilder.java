package com.tvd12.calabash.local.builder;

import com.tvd12.calabash.core.EntityMap;
import com.tvd12.calabash.core.prototype.Prototypes;
import com.tvd12.calabash.local.executor.EntityMapPersistExecutor;
import com.tvd12.calabash.local.impl.EntityMapImpl;
import com.tvd12.calabash.local.setting.EntityMapSetting;
import com.tvd12.ezyfox.builder.EzyBuilder;

import lombok.Getter;

@Getter
@SuppressWarnings("rawtypes")
public class EntityMapBuilder implements EzyBuilder<EntityMap> {
	
	protected Prototypes prototypes;
	protected EntityMapSetting mapSetting;
	protected EntityMapPersistExecutor mapPersistExecutor;
	
	public EntityMapBuilder prototypes(Prototypes prototypes) {
		this.prototypes = prototypes;
		return this;
	}
	
	public EntityMapBuilder mapSetting(EntityMapSetting mapSetting) {
		this.mapSetting = mapSetting;
		return this;
	}
	
	public EntityMapBuilder mapPersistExecutor(EntityMapPersistExecutor mapPersistExecutor) {
		this.mapPersistExecutor = mapPersistExecutor;
		return this;
	}
	
	@Override
	public EntityMap build() {
		return new EntityMapImpl<>(this);
	}
	
}
