package com.tvd12.calabash.eviction;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tvd12.calabash.core.setting.IMapEvictionSetting;

@SuppressWarnings("rawtypes")
public class SimpleMapEviction implements MapEviction {

	protected final long keyMaxIdTime;
	protected final List<Object> evictableKeys;
	protected final Map<Object, Long> keyLastAccessTimes;
	
	public SimpleMapEviction(IMapEvictionSetting setting) {
		this.keyMaxIdTime = setting.getKeyMaxIdleTimeMilis();
		this.keyLastAccessTimes = new HashMap<>();
		this.evictableKeys = new ArrayList<Object>();
	}
	
	@Override
	public void updateKeyTime(Object key) {
		synchronized (this) {
			keyLastAccessTimes.put(key, System.currentTimeMillis());
		}
	}
	
	@Override
	public void updateKeysTime(Collection keys) {
		synchronized (this) {
			for(Object key : keys)
				keyLastAccessTimes.put(key, System.currentTimeMillis());
		}
	}
	
	@Override
	public void removeKey(Object key) {
		synchronized (this) {
			keyLastAccessTimes.remove(key);
		}
	}
	
	@Override
	public void removeKeys(Collection keys) {
		synchronized (this) {
			for(Object key : keys)
				keyLastAccessTimes.remove(key);
		}
	}
	
	@Override
	public List getEvictableKeys() {
		evictableKeys.clear();
		long currentTime = System.currentTimeMillis();
		synchronized (this) {
			for(Object key : keyLastAccessTimes.keySet()) {
				long lastAccesstime = keyLastAccessTimes.get(key);
				long idleTime = currentTime - lastAccesstime;
				if(idleTime > keyMaxIdTime) {
					evictableKeys.add(key);
				}
			}
			for(Object key : evictableKeys) {
				keyLastAccessTimes.remove(key);
			}
		}
		return evictableKeys;
	}
	
}
