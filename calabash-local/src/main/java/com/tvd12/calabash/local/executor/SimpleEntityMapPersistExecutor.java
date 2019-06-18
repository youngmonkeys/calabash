package com.tvd12.calabash.local.executor;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.tvd12.calabash.core.EntityMapPersist;
import com.tvd12.calabash.core.persist.PersistAction;
import com.tvd12.calabash.core.persist.PersistActionQueue;
import com.tvd12.calabash.local.manager.EntityMapPersistManager;
import com.tvd12.calabash.local.persist.PersistActionQueueManager;
import com.tvd12.calabash.local.persist.PersistDeleteManyAction;
import com.tvd12.calabash.local.persist.PersistDeleteOneAction;
import com.tvd12.calabash.local.persist.PersistSaveManyAction;
import com.tvd12.calabash.local.persist.PersistSaveOneAction;
import com.tvd12.calabash.local.setting.EntityMapPersistSetting;
import com.tvd12.calabash.local.setting.EntityMapSetting;
import com.tvd12.ezyfox.builder.EzyBuilder;
import com.tvd12.ezyfox.util.EzyLoggable;

@SuppressWarnings({"rawtypes", "unchecked"})
public class SimpleEntityMapPersistExecutor
		extends EzyLoggable
		implements EntityMapPersistExecutor {
	
	protected final EntityMapPersistManager mapPersistManager;
	protected final PersistActionQueueManager actionQueueManager;
	
	public SimpleEntityMapPersistExecutor(Builder builder) {
		this.mapPersistManager = builder.mapPersistManager;
		this.actionQueueManager = builder.actionQueueManager;
	}

	@Override
	public Map loadAll(EntityMapSetting mapSetting) {
		EntityMapPersist mapPersist = getMapPersist(mapSetting);
		if(mapPersist != null) {
			Map all = mapPersist.loadAll();
			return all;
		}
		return new HashMap<>();
	}

	@Override
	public Map load(EntityMapSetting mapSetting, Set keys) {
		EntityMapPersist mapPersist = getMapPersist(mapSetting);
		if(mapPersist != null) {
			Map all = mapPersist.load(keys);
			return all;
		}
		return new HashMap<>();
	}

	@Override
	public Object load(EntityMapSetting mapSetting, Object key) {
		EntityMapPersist mapPersist = getMapPersist(mapSetting);
		if(mapPersist != null) {
			Object value = mapPersist.load(key);
			return value;
		}
		return null;
	}

	@Override
	public void persist(EntityMapSetting mapSetting, Object key, Object value) {
		EntityMapPersist mapPersist = getMapPersist(mapSetting);
		if(mapPersist != null) {
			PersistSaveOneAction action = new PersistSaveOneAction(key, value);
			addPersistActionToQueue(mapSetting, action);
		}
	}

	@Override
	public void persist(EntityMapSetting mapSetting, Map m) {
		EntityMapPersist mapPersist = getMapPersist(mapSetting);
		if(mapPersist != null) {
			PersistSaveManyAction action = new PersistSaveManyAction(m);
			addPersistActionToQueue(mapSetting, action);
		}
	}

	@Override
	public void delete(EntityMapSetting mapSetting, Object key) {
		EntityMapPersist mapPersist = getMapPersist(mapSetting);
		if(mapPersist != null) {
			PersistDeleteOneAction action = new PersistDeleteOneAction(key);
			addPersistActionToQueue(mapSetting, action);
		}
	}

	@Override
	public void delete(EntityMapSetting mapSetting, Set keys) {
		EntityMapPersist mapPersist = getMapPersist(mapSetting);
		if(mapPersist != null) {
			PersistDeleteManyAction action = new PersistDeleteManyAction(keys);
			addPersistActionToQueue(mapSetting, action);
		}
	}
	
	protected EntityMapPersist getMapPersist(EntityMapSetting setting) {
		EntityMapPersist mp = mapPersistManager.getMapPersist(setting.getMapName());
		return mp;
	}
	
	protected void addPersistActionToQueue(EntityMapSetting setting, PersistAction action) {
		String mapName = setting.getMapName();
		EntityMapPersistSetting persistSetting = setting.getPersistSetting();
		long writeDelay = persistSetting.getWriteDelay();
		PersistActionQueue queue = writeDelay > 0 
				? actionQueueManager.getDelayedQueue(mapName)
				: actionQueueManager.getImmediateQueue(mapName);
		queue.add(action);
	}
	
	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder implements EzyBuilder<EntityMapPersistExecutor> {
		
		protected EntityMapPersistManager mapPersistManager;
		protected PersistActionQueueManager actionQueueManager;
		
		public Builder mapPersistManager(EntityMapPersistManager mapPersistManager) {
			this.mapPersistManager = mapPersistManager;
			return this;
		}
		
		public Builder actionQueueManager(PersistActionQueueManager actionQueueManager) {
			this.actionQueueManager = actionQueueManager;
			return this;
		}
		
		@Override
		public EntityMapPersistExecutor build() {
			return new SimpleEntityMapPersistExecutor(this);
		}
	}

}
