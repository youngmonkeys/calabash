package com.tvd12.calabash.persist.factory;

import com.tvd12.calabash.persist.BytesMapPersist;
import com.tvd12.calabash.persist.EntityBytesMapPersist;
import com.tvd12.calabash.persist.EntityMapPersist;
import com.tvd12.calabash.persist.NameIdMapPersist;
import com.tvd12.ezyfox.builder.EzyBuilder;
import com.tvd12.ezyfox.codec.EzyEntityCodec;

public class EntityBytesMapPersistFactory 
		implements BytesMapPersistFactory {

	protected final EzyEntityCodec entityCodec;
	protected final EntityMapPersistFactory entityMapPersistFactory;
	
	public EntityBytesMapPersistFactory(Builder builder) {
		this.entityCodec = builder.entityCodec;
		this.entityMapPersistFactory = builder.entityMapPersistFactory;
	}
	
	@Override
	public BytesMapPersist newMapPersist(String mapName) {
		EntityMapPersist<?, ?> entityMapPersist = 
				entityMapPersistFactory.newMapPersist(mapName);
		if(entityMapPersist == null)
			return null;
		return EntityBytesMapPersist.builder()
				.entityCodec(entityCodec)
				.entityMapPersist(entityMapPersist)
				.build();
	}
	
	@Override
	public NameIdMapPersist newMapNameIdMapPersist() {
		return entityMapPersistFactory.newMapNameIdMapPersist();
	}
	
	@Override
	public NameIdMapPersist newAtomicLongNameIdMapPersist() {
		return entityMapPersistFactory.newAtomicLongNameIdMapPersist();
	}
	
	@Override
	public NameIdMapPersist newMessageChannelNameIdMapPersist() {
		return entityMapPersistFactory.newMessageChannelNameIdMapPersist();
	}
	
	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder implements EzyBuilder<EntityBytesMapPersistFactory> {
		protected EzyEntityCodec entityCodec;
		protected EntityMapPersistFactory entityMapPersistFactory;
		
		public Builder entityCodec(EzyEntityCodec entityCodec) {
			this.entityCodec = entityCodec;
			return this;
		}
		public Builder entityMapPersistFactory(EntityMapPersistFactory entityMapPersistFactory) {
			this.entityMapPersistFactory = entityMapPersistFactory;
			return this;
		}
		
		@Override
		public EntityBytesMapPersistFactory build() {
			return new EntityBytesMapPersistFactory(this);
		}
	}
}
