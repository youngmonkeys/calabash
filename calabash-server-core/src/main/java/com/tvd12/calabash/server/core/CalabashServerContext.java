package com.tvd12.calabash.server.core;

import java.util.HashMap;
import java.util.Map;

import com.tvd12.calabash.Calabash;
import com.tvd12.calabash.converter.BytesLongConverter;
import com.tvd12.calabash.core.BytesMap;
import com.tvd12.calabash.core.IAtomicLong;
import com.tvd12.calabash.core.manager.MapEvictionManager;
import com.tvd12.calabash.core.statistic.StatisticsAware;
import com.tvd12.calabash.persist.action.PersistActionQueueFactory;
import com.tvd12.calabash.persist.action.PersistActionQueueManager;
import com.tvd12.calabash.persist.bulk.SimplePersistActionHandlingLoop;
import com.tvd12.calabash.persist.factory.BytesMapPersistFactory;
import com.tvd12.calabash.persist.factory.SimpleBytesMapPersistFactory;
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
import com.tvd12.calabash.server.core.manager.MessageChannelManager;
import com.tvd12.calabash.server.core.manager.NameIdManager;
import com.tvd12.calabash.server.core.manager.SimpleAtomicLongManager;
import com.tvd12.calabash.server.core.manager.SimpleBytesMapManager;
import com.tvd12.calabash.server.core.message.MessageChannel;
import com.tvd12.calabash.server.core.setting.Settings;
import com.tvd12.calabash.server.core.setting.SimpleSettings;
import com.tvd12.ezyfox.builder.EzyBuilder;
import com.tvd12.ezyfox.util.EzyLoggable;

public class CalabashServerContext extends EzyLoggable implements Calabash, StatisticsAware {
	
	protected final Settings settings;
	protected final BytesMapFactory mapFactory;
	protected final BytesMapManager mapManager;
	protected final NameIdManager mapNameManager;
	protected final NameIdManager atomicLongNameManager;
	protected final NameIdManager messageChannelNameManager;
	protected final MessageChannelManager messageChannelManager;
	protected final MapPersistManager mapPersistManager;
	protected final AtomicLongManager atomicLongManager;
	protected final MapEvictionManager mapEvictionManager;
	protected final BytesLongConverter bytesLongConverter;
	protected final BytesMapBackupExecutor mapBackupExecutor;
	protected final BytesMapPersistExecutor mapPersistExecutor;
	protected final BytesMapPersistFactory bytesMapPersistFactory;
	protected final PersistActionQueueFactory persistActionQueueFactory;
	protected final PersistActionQueueManager persistActionQueueManager;
	protected final PersistActionHandlingLoop persistActionHandlingLoop;
	
	public CalabashServerContext(Builder builder) {
		this.settings = builder.settings;
		this.bytesLongConverter = builder.bytesLongConverter;
		this.bytesMapPersistFactory = builder.bytesMapPersistFactory;
		this.messageChannelManager = builder.messageChannelManager;
		this.mapNameManager = newMapNameManager();
		this.atomicLongNameManager = newAtomicLongNameManager();
		this.messageChannelNameManager = newMessageChannelNameManager();
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
	
	protected NameIdManager newMapNameManager() {
		return new NameIdManager(
				bytesMapPersistFactory.newMapNameIdMapPersist()
		);
	}
	
	protected NameIdManager newAtomicLongNameManager() {
		return new NameIdManager(
				bytesMapPersistFactory.newAtomicLongNameIdMapPersist()
		);
	}
	
	protected NameIdManager newMessageChannelNameManager() {
		return new NameIdManager(
				bytesMapPersistFactory.newMessageChannelNameIdMapPersist()
		);
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
				.bytesLongConverter(bytesLongConverter)
				.mapPersistManager(mapPersistManager)
				.mapBackupExecutor(mapBackupExecutor)
				.mapPersistExecutor(mapPersistExecutor)
				.bytesMapPersistFactory(bytesMapPersistFactory)
				.build();
	}
	
	protected BytesMapManager newMapManager() {
		return SimpleBytesMapManager.builder()
				.mapFactory(mapFactory)
				.build();
	}
	
	protected PersistActionHandlingLoop newPersistActionHandlingLoop() {
		return SimplePersistActionHandlingLoop.builder()
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
	
	public BytesMap getBytesMap(int id) {
		String name = mapNameManager.getName(id);
		if(name == null)
			return null;
		return mapManager.getMap(name);
	}
	
	public int getMapId(String mapName) {
		return mapNameManager.getId(mapName);
	}
	
	public String getMapName(int mapId) {
		return mapNameManager.getName(mapId);
	}
	
	@Override
	public IAtomicLong getAtomicLong(String name) {
		IAtomicLong atomicLong = atomicLongManager.getAtomicLong(name);
		return atomicLong;
	}
	
	public IAtomicLong getAtomicLong(int id) {
		String name = atomicLongNameManager.getName(id);
		if(name == null)
			return null;
		return atomicLongManager.getAtomicLong(name);
	} 
	
	public int getAtomicLongId(String atomicLongName) {
		return atomicLongNameManager.getId(atomicLongName);
	}
	
	public String getAtomicLongName(int atomicLongId) {
		return atomicLongNameManager.getName(atomicLongId);
	}
	
	public MessageChannel getMessageChannel(String name) {
		MessageChannel channel = messageChannelManager.getChannel(name);
		return channel;
	}
	
	public MessageChannel getMessageChannel(int id) {
		String name = messageChannelNameManager.getName(id);
		if(name == null)
			return null;
		return messageChannelManager.getChannel(name);
	}
	
	public int getMessageChannelId(String channelName) {
		return messageChannelNameManager.getId(channelName);
	}
	
	public String getMessageChannelName(int channelId) {
		return messageChannelNameManager.getName(channelId);
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
	
	public static class Builder implements EzyBuilder<CalabashServerContext> {

		protected Settings settings;
		protected BytesLongConverter bytesLongConverter;
		protected MessageChannelManager messageChannelManager;
		protected BytesMapPersistFactory bytesMapPersistFactory;
		
		public Builder settings(Settings settings) {
			this.settings = settings;
			return this;
		}
		
		public Builder bytesLongConverter(BytesLongConverter bytesLongConverter) {
			this.bytesLongConverter = bytesLongConverter;
			return this;
		}
		
		public Builder messageChannelManager(MessageChannelManager messageChannelManager) {
			this.messageChannelManager = messageChannelManager;
			return this;
		}
		
		public Builder bytesMapPersistFactory(BytesMapPersistFactory bytesMapPersistFactory) {
			this.bytesMapPersistFactory = bytesMapPersistFactory;
			return this;
		}
		
		@Override
		public CalabashServerContext build() {
			if(settings == null)
				settings = new SimpleSettings();
			if(bytesLongConverter == null)
				bytesLongConverter = BytesLongConverter.DEFAULT;
			if(bytesMapPersistFactory == null)
				bytesMapPersistFactory = SimpleBytesMapPersistFactory.builder().build();
			return new CalabashServerContext(this);
		}
	}
}
