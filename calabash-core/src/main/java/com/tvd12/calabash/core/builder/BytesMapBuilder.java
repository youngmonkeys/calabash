package com.tvd12.calabash.core.builder;

import com.tvd12.calabash.core.BytesMap;
import com.tvd12.calabash.core.excecutor.BytesMapBackupExecutor;
import com.tvd12.calabash.core.excecutor.BytesMapPersistExecutor;
import com.tvd12.calabash.core.impl.BytesMapImpl;
import com.tvd12.ezyfox.builder.EzyBuilder;

import lombok.Getter;

@Getter
public class BytesMapBuilder implements EzyBuilder<BytesMap> {
	
	protected String mapName;
	protected BytesMapBackupExecutor mapBackupExecutor;
	protected BytesMapPersistExecutor mapPersistExecutor;
	
	public BytesMapBuilder mapName(String mapName) {
		this.mapName = mapName;
		return this;
	}

	@Override
	public BytesMap build() {
		return new BytesMapImpl(this);
	}
	
}
