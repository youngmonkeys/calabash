package com.tvd12.calabash.backend.factory;

import com.tvd12.calabash.backend.BytesMapPersist;
import com.tvd12.calabash.backend.builder.BytesMapBuilder;
import com.tvd12.calabash.backend.executor.BytesMapBackupExecutor;
import com.tvd12.calabash.backend.executor.BytesMapPersistExecutor;
import com.tvd12.calabash.backend.manager.BytesMapPersistManager;
import com.tvd12.calabash.backend.persist.EntityBytesMapPersist;
import com.tvd12.calabash.backend.setting.Settings;
import com.tvd12.calabash.core.BytesMap;
import com.tvd12.calabash.core.EntityMapPersist;
import com.tvd12.calabash.factory.EntityMapPersistFactory;
import com.tvd12.ezyfox.builder.EzyBuilder;
import com.tvd12.ezyfox.codec.EzyEntityCodec;
import com.tvd12.ezyfox.util.EzyLoggable;

public class SimpleBytesMapFactory extends EzyLoggable implements BytesMapFactory {

	protected final Settings settings;
	protected final EzyEntityCodec entityCodec;
	protected final EntityMapPersistFactory entityMapPersistFactory;
	protected final BytesMapPersistManager mapPersistManager;
	protected final BytesMapBackupExecutor mapBackupExecutor;
	protected final BytesMapPersistExecutor mapPersistExecutor;
	
	protected SimpleBytesMapFactory(Builder builder) {
		this.settings = builder.settings;
		this.entityCodec = builder.entityCodec;
		this.entityMapPersistFactory = builder.entityMapPersistFactory;
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
		EntityMapPersist<?, ?> mapPersist = entityMapPersistFactory.newMapPersist(mapName);
		BytesMapPersist bmp = EntityBytesMapPersist.builder()
				.entityMapPersist(mapPersist)
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
		protected EntityMapPersistFactory entityMapPersistFactory;
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
		
		public Builder entityMapPersistFactory(EntityMapPersistFactory entityMapPersistFactory) {
			this.entityMapPersistFactory = entityMapPersistFactory;
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
