package com.tvd12.calabash.local.executor;

import java.util.Map;
import java.util.Set;

import com.tvd12.calabash.core.query.MapQuery;
import com.tvd12.calabash.local.setting.EntityMapSetting;

@SuppressWarnings("rawtypes")
public interface EntityMapPersistExecutor {

	Map loadAll(EntityMapSetting mapSetting);
	
	Map load(EntityMapSetting mapSetting, Set keys);
	
	Object load(EntityMapSetting mapSetting, Object key);
	
	Object loadByQuery(EntityMapSetting mapSetting, MapQuery query);
	
	void persist(EntityMapSetting mapSetting, Object key, Object value);
	
	void persist(EntityMapSetting mapSetting, Map m);
	
	void delete(EntityMapSetting mapSetting, Object key);
	
	void delete(EntityMapSetting mapSetting, Set keys);
	
}
