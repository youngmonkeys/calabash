package com.tvd12.calabash.backend.impl;

import com.tvd12.calabash.Calabash;
import com.tvd12.calabash.backend.builder.CalabashBuilder;
import com.tvd12.calabash.backend.executor.BytesMapBackupExecutor;
import com.tvd12.calabash.backend.executor.BytesMapPersistExecutor;
import com.tvd12.calabash.backend.executor.SimpleBytesMapBackupExecutor;
import com.tvd12.calabash.backend.executor.SimpleBytesMapPersistExecutor;
import com.tvd12.calabash.backend.factory.BytesMapFactory;
import com.tvd12.calabash.backend.factory.SimpleBytesMapFactory;
import com.tvd12.calabash.backend.manager.BytesMapManager;
import com.tvd12.calabash.backend.manager.BytesMapPersistManager;
import com.tvd12.calabash.backend.manager.SimpleBytesMapManager;
import com.tvd12.calabash.backend.manager.SimpleBytesMapPersistManager;
import com.tvd12.calabash.backend.persist.PersistActionHandlingLoop;
import com.tvd12.calabash.backend.persist.PersistActionQueueFactory;
import com.tvd12.calabash.backend.persist.PersistActionQueueManager;
import com.tvd12.calabash.backend.setting.Settings;
import com.tvd12.calabash.core.BytesMap;
import com.tvd12.calabash.factory.EntityMapPersistFactory;
import com.tvd12.ezyfox.codec.EzyEntityCodec;
import com.tvd12.ezyfox.util.EzyLoggable;

public class CalabashImpl extends EzyLoggable implements Calabash {
	
	protected final Settings settings;
	protected final EzyEntityCodec entityCodec;
	protected final BytesMapFactory mapFactory;
	protected final BytesMapManager mapManager;
	protected final EntityMapPersistFactory entityMapPersistFactory;
	protected final BytesMapPersistManager mapPersistManager;
	protected final BytesMapBackupExecutor mapBackupExecutor;
	protected final BytesMapPersistExecutor mapPersistExecutor;
	protected final PersistActionQueueFactory persistActionQueueFactory;
	protected final PersistActionQueueManager persistActionQueueManager;
	protected final PersistActionHandlingLoop persistActionHandlingLoop;
	
	public CalabashImpl(CalabashBuilder builder) {
		this.settings = builder.getSettings();
		this.entityCodec = builder.getEntityCodec();
		this.entityMapPersistFactory = builder.getMapPersistFactory();
		this.mapPersistManager = newMapPersistManager();
		this.mapBackupExecutor = newMapBackupExecutor();
		this.persistActionQueueFactory = newPersistActionQueueFactory();
		this.persistActionQueueManager = newPersistActionQueueManager();
		this.mapPersistExecutor = newMapPersistExecutor();
		this.mapFactory = newMapFactory();
		this.mapManager = newMapManager();
		this.persistActionHandlingLoop = newPersistActionHandlingLoop();
		this.startAllLoops();
	}
	
	protected BytesMapBackupExecutor newMapBackupExecutor() {
		return new SimpleBytesMapBackupExecutor();
	}
	
	protected BytesMapPersistManager newMapPersistManager() {
		return new SimpleBytesMapPersistManager();
	}
	
	protected PersistActionQueueFactory newPersistActionQueueFactory() {
		return new PersistActionQueueFactory(settings);
	}
	
	protected PersistActionQueueManager newPersistActionQueueManager() {
		return new PersistActionQueueManager(persistActionQueueFactory);
	}
	
	protected BytesMapPersistExecutor newMapPersistExecutor() {
		return SimpleBytesMapPersistExecutor.builder()
				.mapPersistManager(mapPersistManager)
				.actionQueueManager(persistActionQueueManager)
				.build();
	}
	
	protected BytesMapFactory newMapFactory() {
		return SimpleBytesMapFactory.builder()
				.settings(settings)
				.entityCodec(entityCodec)
				.entityMapPersistFactory(entityMapPersistFactory)
				.mapPersistManager(mapPersistManager)
				.mapBackupExecutor(mapBackupExecutor)
				.mapPersistExecutor(mapPersistExecutor)
				.build();
	}
	
	protected BytesMapManager newMapManager() {
		return SimpleBytesMapManager.builder()
				.mapFactory(mapFactory)
				.build();
	}
	
	protected PersistActionHandlingLoop newPersistActionHandlingLoop() {
		return PersistActionHandlingLoop.builder()
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
	public BytesMap getMap(String name) {
		BytesMap map = mapManager.getMap(name);
		return map;
	}

}
