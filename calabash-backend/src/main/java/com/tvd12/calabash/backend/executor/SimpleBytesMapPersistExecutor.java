package com.tvd12.calabash.backend.executor;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.tvd12.calabash.backend.BytesMapPersist;
import com.tvd12.calabash.backend.manager.BytesMapPersistManager;
import com.tvd12.calabash.backend.setting.MapPersistSetting;
import com.tvd12.calabash.backend.setting.MapSetting;
import com.tvd12.calabash.core.util.ByteArray;
import com.tvd12.ezyfox.builder.EzyBuilder;
import com.tvd12.ezyfox.util.EzyLoggable;

public class SimpleBytesMapPersistExecutor
		extends EzyLoggable
		implements BytesMapPersistExecutor {
	
	protected final BytesMapPersistManager mapPersistManager;
	
	public SimpleBytesMapPersistExecutor(Builder builder) {
		this.mapPersistManager = builder.mapPersistManager;
	}

	@Override
	public Map<ByteArray, byte[]> loadAll(MapSetting mapSetting) {
		BytesMapPersist mapPersist = getMapPersist(mapSetting);
		if(mapPersist != null) {
			Map<ByteArray, byte[]> all = mapPersist.loadAll();
			return all;
		}
		return new HashMap<>();
	}

	@Override
	public Map<ByteArray, byte[]> load(MapSetting mapSetting, Set<ByteArray> keys) {
		BytesMapPersist mapPersist = getMapPersist(mapSetting);
		if(mapPersist != null) {
			Map<ByteArray, byte[]> all = mapPersist.load(keys);
			return all;
		}
		return new HashMap<>();
	}

	@Override
	public byte[] load(MapSetting mapSetting, ByteArray key) {
		BytesMapPersist mapPersist = getMapPersist(mapSetting);
		if(mapPersist != null) {
			byte[] value = mapPersist.load(key);
			return value;
		}
		return null;
	}

	@Override
	public void persist(MapSetting mapSetting, ByteArray key, byte[] value) {
		BytesMapPersist mapPersist = getMapPersist(mapSetting);
		if(mapPersist != null) {
			MapPersistSetting setting = mapSetting.getPersistSetting();
			if(setting.isAsync()) {
				
			}
			else {
				mapPersist.persist(key, value);
			}
		}
		
	}

	@Override
	public void persist(MapSetting mapSetting, Map<ByteArray, byte[]> m) {
		BytesMapPersist mapPersist = getMapPersist(mapSetting);
		if(mapPersist != null) {
			MapPersistSetting setting = mapSetting.getPersistSetting();
			if(!setting.isAsync()) {
				mapPersist.persist(m);
			}
		}
		
	}

	@Override
	public void delete(MapSetting mapSetting, ByteArray key) {
		BytesMapPersist mapPersist = getMapPersist(mapSetting);
		if(mapPersist != null) {
			MapPersistSetting setting = mapSetting.getPersistSetting();
			if(!setting.isAsync()) {
				mapPersist.delete(key);
			}
		}
		
	}

	@Override
	public void delete(MapSetting mapSetting, Set<ByteArray> keys) {
		BytesMapPersist mapPersist = getMapPersist(mapSetting);
		if(mapPersist != null) {
			MapPersistSetting setting = mapSetting.getPersistSetting();
			if(!setting.isAsync()) {
				mapPersist.delete(keys);
			}
		}
		
	}
	
	protected BytesMapPersist getMapPersist(MapSetting setting) {
		BytesMapPersist mp = mapPersistManager.getMapPersist(setting.getMapName());
		return mp;
	}
	
	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder implements EzyBuilder<BytesMapPersistExecutor> {
		
		protected BytesMapPersistManager mapPersistManager;
		
		public Builder mapPersistManager(BytesMapPersistManager mapPersistManager) {
			this.mapPersistManager = mapPersistManager;
			return this;
		}
		
		@Override
		public BytesMapPersistExecutor build() {
			return new SimpleBytesMapPersistExecutor(this);
		}
	}

}
