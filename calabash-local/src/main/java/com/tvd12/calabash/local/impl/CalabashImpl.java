package com.tvd12.calabash.local.impl;

import com.tvd12.calabash.Calabash;
import com.tvd12.calabash.core.EntityMap;
import com.tvd12.calabash.local.builder.CalabashBuilder;
import com.tvd12.calabash.local.executor.EntityMapPersistExecutor;
import com.tvd12.calabash.local.executor.SimpleEntityMapPersistExecutor;
import com.tvd12.calabash.local.factory.EntityMapFactory;
import com.tvd12.calabash.local.factory.SimpleEntityMapFactory;
import com.tvd12.calabash.local.manager.EntityMapManager;
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
public class CalabashImpl extends EzyLoggable implements Calabash {
	
	protected final Settings settings;
	protected final EntityMapFactory mapFactory;
	protected final EntityMapManager mapManager;
	protected final MapPersistManager mapPersistManager;
	protected final EntityMapPersistExecutor mapPersistExecutor;
	protected final EntityMapPersistFactory entityMapPersistFactory;
	protected final PersistActionQueueFactory persistActionQueueFactory;
	protected final PersistActionQueueManager persistActionQueueManager;
	protected final PersistActionHandlingLoop persistActionHandlingLoop;
	
	public CalabashImpl(CalabashBuilder builder) {
		this.settings = builder.getSettings();
		this.entityMapPersistFactory = builder.getEntityMapPersistFactory();
		this.mapPersistManager = newMapPersistManager();
		this.persistActionQueueFactory = newPersistActionQueueFactory();
		this.persistActionQueueManager = newPersistActionQueueManager();
		this.mapPersistExecutor = newMapPersistExecutor();
		this.mapFactory = newMapFactory();
		this.mapManager = newMapManager();
		this.persistActionHandlingLoop = newPersistActionHandlingLoop();
		this.startAllLoops();
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
				.mapPersistManager(mapPersistManager)
				.mapPersistExecutor(mapPersistExecutor)
				.entityMapPersistFactory(entityMapPersistFactory)
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
	
	protected void startAllLoops() {
		try {
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

}
