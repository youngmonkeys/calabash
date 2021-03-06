package com.tvd12.calabash.persist.factory;

import java.util.HashMap;
import java.util.Map;

import com.tvd12.calabash.persist.EntityMapPersist;
import com.tvd12.calabash.persist.NameIdMapPersist;
import com.tvd12.calabash.persist.SimpleNameIdMapPersist;
import com.tvd12.ezyfox.builder.EzyBuilder;

@SuppressWarnings("rawtypes")
public class SimpleEntityMapPersistFactory implements EntityMapPersistFactory {
	protected final NameIdMapPersist mapNameIdMapPersist;
	protected final NameIdMapPersist atomicLongNameIdMapPersist;
	protected final NameIdMapPersist messageChannelNameIdMapPersist;
	protected final Map<String, EntityMapPersist> entityMapPersists;
	
	public SimpleEntityMapPersistFactory(Builder builder) {
		this.entityMapPersists = builder.entityMapPersists;
		this.mapNameIdMapPersist = builder.mapNameIdMapPersist;
		this.atomicLongNameIdMapPersist = builder.atomicLongNameIdMapPersist;
		this.messageChannelNameIdMapPersist = builder.messageChannelNameIdMapPersist;
	}
	
	@Override
	public EntityMapPersist<?, ?> newMapPersist(String mapName) {
		EntityMapPersist entityMapPersist = entityMapPersists.get(mapName);
		if(entityMapPersist != null)
			return entityMapPersist;
		return null;
	}
	
	@Override
	public NameIdMapPersist newMapNameIdMapPersist() {
		return mapNameIdMapPersist;
	}
	
	@Override
	public NameIdMapPersist newAtomicLongNameIdMapPersist() {
		return atomicLongNameIdMapPersist;
	}
	
	@Override
	public NameIdMapPersist newMessageChannelNameIdMapPersist() {
		return messageChannelNameIdMapPersist;
	}
	
	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder implements EzyBuilder<EntityMapPersistFactory> {
		protected NameIdMapPersist mapNameIdMapPersist;
		protected NameIdMapPersist atomicLongNameIdMapPersist;
		protected NameIdMapPersist messageChannelNameIdMapPersist;
		protected Map<String, EntityMapPersist> entityMapPersists;
		
		public Builder() {
			this.entityMapPersists = new HashMap<>();
		}
		
		public void addMapPersist(String mapName, EntityMapPersist entityMapPersist) {
			this.entityMapPersists.put(mapName, entityMapPersist);
		}
		
		public Builder mapNameIdMapPersist(NameIdMapPersist mapNameIdMapPersist) {
			this.mapNameIdMapPersist = mapNameIdMapPersist;
			return this;
		}
		
		public Builder atomicLongNameIdMapPersist(NameIdMapPersist atomicLongNameIdMapPersist) {
			this.atomicLongNameIdMapPersist = atomicLongNameIdMapPersist;
			return this;
		}
		
		public Builder messageChannelNameIdMapPersist(NameIdMapPersist messageChannelNameIdMapPersist) {
			this.messageChannelNameIdMapPersist = messageChannelNameIdMapPersist;
			return this;
		}

		@Override
		public EntityMapPersistFactory build() {
			if(mapNameIdMapPersist == null)
				mapNameIdMapPersist = new SimpleNameIdMapPersist();
			if(atomicLongNameIdMapPersist == null)
				atomicLongNameIdMapPersist = new SimpleNameIdMapPersist();
			if(messageChannelNameIdMapPersist == null)
				messageChannelNameIdMapPersist = new SimpleNameIdMapPersist();
			return new SimpleEntityMapPersistFactory(this);
		}
	}
	
}
