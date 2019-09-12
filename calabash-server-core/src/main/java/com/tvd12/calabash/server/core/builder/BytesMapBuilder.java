package com.tvd12.calabash.server.core.builder;

import com.tvd12.calabash.core.BytesMap;
import com.tvd12.calabash.server.core.executor.BytesMapBackupExecutor;
import com.tvd12.calabash.server.core.executor.BytesMapPersistExecutor;
import com.tvd12.calabash.server.core.impl.BytesMapImpl;
import com.tvd12.calabash.server.core.setting.MapSetting;
import com.tvd12.ezyfox.builder.EzyBuilder;

import lombok.Getter;

@Getter
public class BytesMapBuilder implements EzyBuilder<BytesMap> {
	
	protected MapSetting mapSetting;
	protected BytesMapBackupExecutor mapBackupExecutor;
	protected BytesMapPersistExecutor mapPersistExecutor;
	
	public BytesMapBuilder mapSetting(MapSetting mapSetting) {
		this.mapSetting = mapSetting;
		return this;
	}
	
	public BytesMapBuilder mapBackupExecutor(BytesMapBackupExecutor mapBackupExecutor) {
		this.mapBackupExecutor = mapBackupExecutor;
		return this;
	}
	
	public BytesMapBuilder mapPersistExecutor(BytesMapPersistExecutor mapPersistExecutor) {
		this.mapPersistExecutor = mapPersistExecutor;
		return this;
	}

	@Override
	public BytesMap build() {
		return new BytesMapImpl(this);
	}
	
}
