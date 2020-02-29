package com.tvd12.calabash.local;

import java.util.HashMap;
import java.util.Map;

import com.tvd12.calabash.Calabash;
import com.tvd12.calabash.core.EntityMap;
import com.tvd12.calabash.core.IAtomicLong;
import com.tvd12.calabash.core.manager.MapEvictionManager;
import com.tvd12.calabash.core.prototype.Prototypes;
import com.tvd12.calabash.core.statistic.StatisticsAware;
import com.tvd12.calabash.local.executor.EntityMapPersistExecutor;
import com.tvd12.calabash.local.executor.SimpleEntityMapPersistExecutor;
import com.tvd12.calabash.local.factory.EntityMapFactory;
import com.tvd12.calabash.local.factory.SimpleEntityMapFactory;
import com.tvd12.calabash.local.manager.AtomicLongManager;
import com.tvd12.calabash.local.manager.EntityMapManager;
import com.tvd12.calabash.local.manager.SimpleAtomicLongManager;
import com.tvd12.calabash.local.manager.SimpleEntityMapManager;
import com.tvd12.calabash.local.persist.PersistActionHandlingLoopImpl;
import com.tvd12.calabash.local.setting.Settings;
import com.tvd12.calabash.persist.action.PersistActionQueueFactory;
import com.tvd12.calabash.persist.action.PersistActionQueueManager;
import com.tvd12.calabash.persist.factory.EntityMapPersistFactory;
import com.tvd12.calabash.persist.handler.PersistActionHandlingLoop;
import com.tvd12.calabash.persist.manager.MapPersistManager;
import com.tvd12.calabash.persist.manager.SimpleMapPersistManager;
import com.tvd12.ezyfox.util.EzyLoggable;

@SuppressWarnings("unchecked")
public class CalabashLocal extends EzyLoggable implements Calabash, StatisticsAware {
	
	protected final Settings settings;
	protected final Prototypes prototypes;
	protected final EntityMapFactory mapFactory;
	protected final EntityMapManager mapManager;
	protected final MapPersistManager mapPersistManager;
	protected final AtomicLongManager atomicLongManager;
	protected final MapEvictionManager mapEvictionManager;
	protected final EntityMapPersistFactory mapPersistFactory;
	protected final EntityMapPersistExecutor mapPersistExecutor;
	protected final PersistActionQueueFactory persistActionQueueFactory;
	protected final PersistActionQueueManager persistActionQueueManager;
	protected final PersistActionHandlingLoop persistActionHandlingLoop;
	
	protected CalabashLocal(CalabashBuilder builder) {
		this.settings = builder.getSettings();
		this.prototypes = builder.getPrototypes();
		this.mapPersistFactory = builder.getMapPersistFactory();
		this.mapPersistManager = newMapPersistManager();
		this.persistActionQueueFactory = newPersistActionQueueFactory();
		this.persistActionQueueManager = newPersistActionQueueManager();
		this.mapPersistExecutor = newMapPersistExecutor();
		this.mapFactory = newMapFactory();
		this.mapManager = newMapManager();
		this.mapEvictionManager = newMapEvictionManager();
		this.atomicLongManager = newAtomicLongManager();
		this.persistActionHandlingLoop = newPersistActionHandlingLoop();
		this.startAllComponents();
	}
	
	protected MapPersistManager newMapPersistManager() {
		return new SimpleMapPersistManager();
	}
	
	protected PersistActionQueueFactory newPersistActionQueueFactory() {
		return new PersistActionQueueFactory(settings);
	}
	
	protected PersistActionQueueManager newPersistActionQueueManager() {
		return new PersistActionQueueManager(persistActionQueueFactory);
	}
	
	protected EntityMapPersistExecutor newMapPersistExecutor() {
		return SimpleEntityMapPersistExecutor.builder()
				.mapPersistManager(mapPersistManager)
				.actionQueueManager(persistActionQueueManager)
				.build();
	}
	
	protected EntityMapFactory newMapFactory() {
		return SimpleEntityMapFactory.builder()
				.settings(settings)
				.prototype(prototypes)
				.mapPersistManager(mapPersistManager)
				.mapPersistFactory(mapPersistFactory)
				.mapPersistExecutor(mapPersistExecutor)
				.build();
	}
	
	protected EntityMapManager newMapManager() {
		return SimpleEntityMapManager.builder()
				.mapFactory(mapFactory)
				.build();
	}
	
	protected PersistActionHandlingLoop newPersistActionHandlingLoop() {
		return PersistActionHandlingLoopImpl.builder()
				.mapPersistManager(mapPersistManager)
				.actionQueueManager(persistActionQueueManager)
				.build();
	}
	
	protected MapEvictionManager newMapEvictionManager() {
		return MapEvictionManager.builder()
				.mapManager(mapManager)
				.evictionInterval(settings.getMapEvictionInterval())
				.build();
	}
	
	protected AtomicLongManager newAtomicLongManager() {
		String mapName = settings.getAtomicLongMapName();
		EntityMap<String, Long> map = getEntityMap(mapName);
		return new SimpleAtomicLongManager(map);
	}
	
	protected void startAllComponents() {
		try {
			mapEvictionManager.start();
			persistActionHandlingLoop.start();
		} catch (Exception e) {
			throw new RuntimeException("start all loops failed", e);
		}
	}

	@Override
	public <K,V> EntityMap<K,V> getEntityMap(String name) {
		EntityMap<K,V> map = mapManager.getMap(name);
		return map;
	}
	
	@Override
	public IAtomicLong getAtomicLong(String name) {
		IAtomicLong atomicLong = atomicLongManager.getAtomicLong(name);
		return atomicLong;
	}
	
	@Override
	public void addStatistics(Map<String, Object> statistics) {
		Map<String, Object> mapManagerStat = new HashMap<>();
		((StatisticsAware)mapManager).addStatistics(mapManagerStat);
		statistics.put("mapManager", mapManagerStat);
	}
	
	public static CalabashBuilder builder() {
		return new CalabashBuilder();
	}

}
