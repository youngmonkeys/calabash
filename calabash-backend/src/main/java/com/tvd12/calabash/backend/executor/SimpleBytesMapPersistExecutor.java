package com.tvd12.calabash.backend.executor;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.tvd12.calabash.backend.BytesMapPersist;
import com.tvd12.calabash.backend.setting.MapPersistSetting;
import com.tvd12.calabash.backend.setting.MapSetting;
import com.tvd12.calabash.core.util.ByteArray;
import com.tvd12.ezyfox.util.EzyLoggable;

public class SimpleBytesMapPersistExecutor
		extends EzyLoggable
		implements BytesMapPersistExecutor {
	
	protected final Map<String, BytesMapPersist> mapPersists;
	
	public SimpleBytesMapPersistExecutor() {
		this.mapPersists = new HashMap<>();
	}

	@Override
	public Map<ByteArray, byte[]> loadAll(MapSetting mapSetting) {
		BytesMapPersist mapPersist = mapPersists.get(mapSetting.getMapName());
		if(mapPersist != null) {
			Map<ByteArray, byte[]> all = mapPersist.loadAll();
			return all;
		}
		return new HashMap<>();
	}

	@Override
	public Map<ByteArray, byte[]> load(MapSetting mapSetting, Set<ByteArray> keys) {
		BytesMapPersist mapPersist = mapPersists.get(mapSetting.getMapName());
		if(mapPersist != null) {
			Map<ByteArray, byte[]> all = mapPersist.load(keys);
			return all;
		}
		return new HashMap<>();
	}

	@Override
	public byte[] load(MapSetting mapSetting, ByteArray key) {
		BytesMapPersist mapPersist = mapPersists.get(mapSetting.getMapName());
		if(mapPersist != null) {
			byte[] value = mapPersist.load(key);
			return value;
		}
		return null;
	}

	@Override
	public void persist(MapSetting mapSetting, ByteArray key, byte[] value) {
		BytesMapPersist mapPersist = mapPersists.get(mapSetting.getMapName());
		if(mapPersist != null) {
			MapPersistSetting setting = mapSetting.getPersistSetting();
			if(!setting.isAsync()) {
				mapPersist.persist(key, value);
			}
		}
		
	}

	@Override
	public void persist(MapSetting mapSetting, Map<ByteArray, byte[]> m) {
		BytesMapPersist mapPersist = mapPersists.get(mapSetting.getMapName());
		if(mapPersist != null) {
			MapPersistSetting setting = mapSetting.getPersistSetting();
			if(!setting.isAsync()) {
				mapPersist.persist(m);
			}
		}
		
	}

	@Override
	public void delete(MapSetting mapSetting, ByteArray key) {
		BytesMapPersist mapPersist = mapPersists.get(mapSetting.getMapName());
		if(mapPersist != null) {
			MapPersistSetting setting = mapSetting.getPersistSetting();
			if(!setting.isAsync()) {
				mapPersist.delete(key);
			}
		}
		
	}

	@Override
	public void delete(MapSetting mapSetting, Set<ByteArray> keys) {
		BytesMapPersist mapPersist = mapPersists.get(mapSetting.getMapName());
		if(mapPersist != null) {
			MapPersistSetting setting = mapSetting.getPersistSetting();
			if(!setting.isAsync()) {
				mapPersist.delete(keys);
			}
		}
		
	}
	
	public void addMapPersist(String mapName, BytesMapPersist mapPersist) {
		this.mapPersists.put(mapName, mapPersist);
	}

}
