package com.tvd12.calabash.server.core;

import java.util.HashMap;
import java.util.Map;

import com.tvd12.calabash.Calabash;
import com.tvd12.calabash.core.BytesMap;
import com.tvd12.calabash.core.IAtomicLong;
import com.tvd12.calabash.core.manager.MapEvictionManager;
import com.tvd12.calabash.core.statistic.StatisticsAware;
import com.tvd12.calabash.persist.action.PersistActionQueueFactory;
import com.tvd12.calabash.persist.action.PersistActionQueueManager;
import com.tvd12.calabash.persist.factory.DefaultEntityMapPersistFactory;
import com.tvd12.calabash.persist.factory.EntityMapPersistFactory;
import com.tvd12.calabash.persist.handler.PersistActionHandlingLoop;
import com.tvd12.calabash.persist.manager.MapPersistManager;
import com.tvd12.calabash.persist.manager.SimpleMapPersistManager;
import com.tvd12.calabash.server.core.executor.BytesMapBackupExecutor;
import com.tvd12.calabash.server.core.executor.BytesMapPersistExecutor;
import com.tvd12.calabash.server.core.executor.SimpleBytesMapBackupExecutor;
import com.tvd12.calabash.server.core.executor.SimpleBytesMapPersistExecutor;
import com.tvd12.calabash.server.core.factory.BytesMapFactory;
import com.tvd12.calabash.server.core.factory.SimpleBytesMapFactory;
import com.tvd12.calabash.server.core.manager.AtomicLongManager;
import com.tvd12.calabash.server.core.manager.BytesMapManager;
import com.tvd12.calabash.server.core.manager.SimpleAtomicLongManager;
import com.tvd12.calabash.server.core.manager.SimpleBytesMapManager;
import com.tvd12.calabash.server.core.persist.PersistActionHandlingLoopImpl;
import com.tvd12.calabash.server.core.setting.Settings;
import com.tvd12.ezyfox.builder.EzyBuilder;
import com.tvd12.ezyfox.codec.EzyEntityCodec;
import com.tvd12.ezyfox.util.EzyLoggable;

public class CalabashServerContext extends EzyLoggable implements Calabash, StatisticsAware {
	
	protected final Settings settings;
	protected final EzyEntityCodec entityCodec;
	protected final BytesMapFactory mapFactory;
	protected final BytesMapManager mapManager;
	protected final MapPersistManager mapPersistManager;
	protected final AtomicLongManager atomicLongManager;
	protected final MapEvictionManager mapEvictionManager;
	protected final BytesMapBackupExecutor mapBackupExecutor;
	protected final BytesMapPersistExecutor mapPersistExecutor;
	protected final EntityMapPersistFactory entityMapPersistFactory;
	protected final PersistActionQueueFactory persistActionQueueFactory;
	protected final PersistActionQueueManager persistActionQueueManager;
	protected final PersistActionHandlingLoop persistActionHandlingLoop;
	
	public CalabashServerContext(Builder builder) {
		this.settings = builder.settings;
		this.entityCodec = builder.entityCodec;
		this.entityMapPersistFactory = builder.entityMapPersistFactory;
		this.mapPersistManager = newMapPersistManager();
		this.mapBackupExecutor = newMapBackupExecutor();
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
	
	protected MapEvictionManager newMapEvictionManager() {
		return MapEvictionManager.builder()
				.mapManager(mapManager)
				.evictionInterval(settings.getMapEvictionInterval())
				.build();
	}
	
	protected AtomicLongManager newAtomicLongManager() {
		String mapName = settings.getAtomicLongMapName();
		BytesMap map = getBytesMap(mapName);
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
	public BytesMap getBytesMap(String name) {
		BytesMap map = mapManager.getMap(name);
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
	
	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder implements EzyBuilder<Calabash> {

		protected Settings settings;
		protected EzyEntityCodec entityCodec;
		protected EntityMapPersistFactory entityMapPersistFactory;
		
		public Builder settings(Settings settings) {
			this.settings = settings;
			return this;
		}
		
		public Builder entityCodec(EzyEntityCodec entityCodec) {
			this.entityCodec = entityCodec;
			return this;
		}
		
		public Builder entityMapPersistFactory(EntityMapPersistFactory entityMapPersistFactory) {
			this.entityMapPersistFactory = entityMapPersistFactory;
			return this;
		}
		
		@Override
		public Calabash build() {
			if(entityMapPersistFactory == null)
				entityMapPersistFactory = new DefaultEntityMapPersistFactory();
			return new CalabashServerContext(this);
		}
		
	}
}
