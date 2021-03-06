package com.tvd12.calabash.server.core.factory;

import com.tvd12.calabash.converter.BytesLongConverter;
import com.tvd12.calabash.core.BytesMap;
import com.tvd12.calabash.persist.BytesMapPersist;
import com.tvd12.calabash.persist.factory.BytesMapPersistFactory;
import com.tvd12.calabash.persist.manager.MapPersistManager;
import com.tvd12.calabash.server.core.executor.BytesMapBackupExecutor;
import com.tvd12.calabash.server.core.executor.BytesMapPersistExecutor;
import com.tvd12.calabash.server.core.impl.BytesMapImpl;
import com.tvd12.calabash.server.core.setting.Settings;
import com.tvd12.ezyfox.builder.EzyBuilder;
import com.tvd12.ezyfox.util.EzyLoggable;

public class SimpleBytesMapFactory extends EzyLoggable implements BytesMapFactory {

	protected final Settings settings;
	protected final MapPersistManager mapPersistManager;
	protected final BytesLongConverter bytesLongConverter;
	protected final BytesMapBackupExecutor mapBackupExecutor;
	protected final BytesMapPersistExecutor mapPersistExecutor;
	protected final BytesMapPersistFactory bytesMapPersistFactory;
	
	protected SimpleBytesMapFactory(Builder builder) {
		this.settings = builder.settings;
		this.bytesLongConverter = builder.bytesLongConverter;
		this.mapPersistManager = builder.mapPersistManager;
		this.mapBackupExecutor = builder.mapBackupExecutor;
		this.mapPersistExecutor = builder.mapPersistExecutor;
		this.bytesMapPersistFactory = builder.bytesMapPersistFactory;
	}
	
	@Override
	public BytesMap newMap(String mapName) {
		BytesMap map = createMap(mapName);
		newMapPersist(mapName);
		return map;
	}
	
	protected BytesMap createMap(String mapName) {
		BytesMap map = BytesMapImpl.builder()
				.bytesLongConverter(bytesLongConverter)
				.mapBackupExecutor(mapBackupExecutor)
				.mapPersistExecutor(mapPersistExecutor)
				.mapSetting(settings.getMapSetting(mapName))
				.build();
		return map;
	}
	
	protected void newMapPersist(String mapName) {
		BytesMapPersist mapPersist = bytesMapPersistFactory.newMapPersist(mapName);
		mapPersistManager.addMapPersist(mapName, mapPersist);
	}
	
	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder implements EzyBuilder<BytesMapFactory> {
		
		protected Settings settings;
		protected MapPersistManager mapPersistManager;
		protected BytesLongConverter bytesLongConverter;
		protected BytesMapPersistFactory bytesMapPersistFactory;
		protected BytesMapBackupExecutor mapBackupExecutor;
		protected BytesMapPersistExecutor mapPersistExecutor;
		
		public Builder settings(Settings settings) {
			this.settings = settings;
			return this;
		}
		
		public Builder bytesLongConverter(BytesLongConverter bytesLongConverter) {
			this.bytesLongConverter = bytesLongConverter;
			return this;
		}
		
		public Builder bytesMapPersistFactory(BytesMapPersistFactory bytesMapPersistFactory) {
			this.bytesMapPersistFactory = bytesMapPersistFactory;
			return this;
		}
		
		public Builder mapPersistManager(MapPersistManager mapPersistManager) {
			this.mapPersistManager = mapPersistManager;
			return this;
		}
		
		public Builder mapBackupExecutor(BytesMapBackupExecutor mapBackupExecutor) {
			this.mapBackupExecutor = mapBackupExecutor;
			return this;
		}
		
		public Builder mapPersistExecutor(BytesMapPersistExecutor mapPersistExecutor) {
			this.mapPersistExecutor = mapPersistExecutor;
			return this;
		}
		
		@Override
		public BytesMapFactory build() {
			return new SimpleBytesMapFactory(this);
		}
	}
	
}
