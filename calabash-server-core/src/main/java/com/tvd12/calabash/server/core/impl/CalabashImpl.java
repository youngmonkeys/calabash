package com.tvd12.calabash.server.core.impl;

import com.tvd12.calabash.Calabash;
import com.tvd12.calabash.core.BytesMap;
import com.tvd12.calabash.persist.action.PersistActionQueueFactory;
import com.tvd12.calabash.persist.action.PersistActionQueueManager;
import com.tvd12.calabash.persist.factory.EntityMapPersistFactory;
import com.tvd12.calabash.persist.handler.PersistActionHandlingLoop;
import com.tvd12.calabash.persist.manager.MapPersistManager;
import com.tvd12.calabash.persist.manager.SimpleMapPersistManager;
import com.tvd12.calabash.server.core.builder.CalabashBuilder;
import com.tvd12.calabash.server.core.executor.BytesMapBackupExecutor;
import com.tvd12.calabash.server.core.executor.BytesMapPersistExecutor;
import com.tvd12.calabash.server.core.executor.SimpleBytesMapBackupExecutor;
import com.tvd12.calabash.server.core.executor.SimpleBytesMapPersistExecutor;
import com.tvd12.calabash.server.core.factory.BytesMapFactory;
import com.tvd12.calabash.server.core.factory.SimpleBytesMapFactory;
import com.tvd12.calabash.server.core.manager.BytesMapManager;
import com.tvd12.calabash.server.core.manager.SimpleBytesMapManager;
import com.tvd12.calabash.server.core.persist.PersistActionHandlingLoopImpl;
import com.tvd12.calabash.server.core.setting.Settings;
import com.tvd12.ezyfox.codec.EzyEntityCodec;
import com.tvd12.ezyfox.util.EzyLoggable;

public class CalabashImpl extends EzyLoggable implements Calabash {
	
	protected final Settings settings;
	protected final EzyEntityCodec entityCodec;
	protected final BytesMapFactory mapFactory;
	protected final BytesMapManager mapManager;
	protected final MapPersistManager mapPersistManager;
	protected final BytesMapBackupExecutor mapBackupExecutor;
	protected final BytesMapPersistExecutor mapPersistExecutor;
	protected final EntityMapPersistFactory entityMapPersistFactory;
	protected final PersistActionQueueFactory persistActionQueueFactory;
	protected final PersistActionQueueManager persistActionQueueManager;
	protected final PersistActionHandlingLoop persistActionHandlingLoop;
	
	public CalabashImpl(CalabashBuilder builder) {
		this.settings = builder.getSettings();
		this.entityCodec = builder.getEntityCodec();
		this.entityMapPersistFactory = builder.getEntityMapPersistFactory();
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
	
	protected MapPersistManager newMapPersistManager() {
		return new SimpleMapPersistManager();
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
				.mapPersistManager(mapPersistManager)
				.mapBackupExecutor(mapBackupExecutor)
				.mapPersistExecutor(mapPersistExecutor)
				.entityMapPersistFactory(entityMapPersistFactory)
				.build();
	}
	
	protected BytesMapManager newMapManager() {
		return SimpleBytesMapManager.builder()
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
	public BytesMap getBytesMap(String name) {
		BytesMap map = mapManager.getMap(name);
		return map;
	}

}
