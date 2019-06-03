package com.tvd12.calabash.backend.factory;

import com.tvd12.calabash.backend.BytesMapPersist;
import com.tvd12.calabash.backend.MapPersist;
import com.tvd12.calabash.backend.builder.BytesMapBuilder;
import com.tvd12.calabash.backend.executor.BytesMapBackupExecutor;
import com.tvd12.calabash.backend.executor.BytesMapPersistExecutor;
import com.tvd12.calabash.backend.manager.BytesMapPersistManager;
import com.tvd12.calabash.backend.persist.EntityBytesMapPersist;
import com.tvd12.calabash.backend.setting.Settings;
import com.tvd12.calabash.core.BytesMap;
import com.tvd12.ezyfox.builder.EzyBuilder;
import com.tvd12.ezyfox.codec.EzyEntityCodec;
import com.tvd12.ezyfox.util.EzyLoggable;

public class SimpleBytesMapFactory extends EzyLoggable implements BytesMapFactory {

	protected final Settings settings;
	protected final EzyEntityCodec entityCodec;
	protected final MapPersistFactory mapPersistFactory;
	protected final BytesMapPersistManager mapPersistManager;
	protected final BytesMapBackupExecutor mapBackupExecutor;
	protected final BytesMapPersistExecutor mapPersistExecutor;
	
	protected SimpleBytesMapFactory(Builder builder) {
		this.settings = builder.settings;
		this.entityCodec = builder.entityCodec;
		this.mapPersistFactory = builder.mapPersistFactory;
		this.mapPersistManager = builder.mapPersistManager;
		this.mapBackupExecutor = builder.mapBackupExecutor;
		this.mapPersistExecutor = builder.mapPersistExecutor;
	}
	
	@Override
	public BytesMap newMap(String mapName) {
		BytesMap map = createMap(mapName);
		newMapPersist(mapName);
		return map;
	}
	
	protected BytesMap createMap(String mapName) {
		BytesMap map = new BytesMapBuilder()
				.mapBackupExecutor(mapBackupExecutor)
				.mapPersistExecutor(mapPersistExecutor)
				.mapSetting(settings.getMapSetting(mapName))
				.build();
		return map;
	}
	
	protected void newMapPersist(String mapName) {
		MapPersist<?, ?> mapPersist = mapPersistFactory.newMapPersist(mapName);
		BytesMapPersist bmp = EntityBytesMapPersist.builder()
				.mapPersist(mapPersist)
				.entityCodec(entityCodec)
				.build();
		mapPersistManager.addMapPersist(mapName, bmp);
	}
	
	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder implements EzyBuilder<BytesMapFactory> {
		
		protected Settings settings;
		protected EzyEntityCodec entityCodec;
		protected MapPersistFactory mapPersistFactory;
		protected BytesMapPersistManager mapPersistManager;
		protected BytesMapBackupExecutor mapBackupExecutor;
		protected BytesMapPersistExecutor mapPersistExecutor;
		
		public Builder settings(Settings settings) {
			this.settings = settings;
			return this;
		}
		
		public Builder entityCodec(EzyEntityCodec entityCodec) {
			this.entityCodec = entityCodec;
			return this;
		}
		
		public Builder mapPersistFactory(MapPersistFactory mapPersistFactory) {
			this.mapPersistFactory = mapPersistFactory;
			return this;
		}
		
		public Builder mapPersistManager(BytesMapPersistManager mapPersistManager) {
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
