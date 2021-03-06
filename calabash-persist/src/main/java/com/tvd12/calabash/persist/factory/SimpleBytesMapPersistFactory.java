package com.tvd12.calabash.persist.factory;

import java.util.HashMap;
import java.util.Map;

import com.tvd12.calabash.persist.BytesMapPersist;
import com.tvd12.calabash.persist.NameIdMapPersist;
import com.tvd12.calabash.persist.SimpleNameIdMapPersist;
import com.tvd12.ezyfox.builder.EzyBuilder;

public class SimpleBytesMapPersistFactory implements BytesMapPersistFactory {
	protected final NameIdMapPersist mapNameIdMapPersist;
	protected final NameIdMapPersist atomicLongNameIdMapPersist;
	protected final NameIdMapPersist messageChannelNameIdMapPersist;
	protected final Map<String, BytesMapPersist> bytesMapPersists;
	
	public SimpleBytesMapPersistFactory(Builder builder) {
		this.bytesMapPersists = builder.bytesMapPersists;
		this.mapNameIdMapPersist = builder.mapNameIdMapPersist;
		this.atomicLongNameIdMapPersist = builder.atomicLongNameIdMapPersist;
		this.messageChannelNameIdMapPersist = builder.messageChannelNameIdMapPersist;
	}
	
	@Override
	public BytesMapPersist newMapPersist(String mapName) {
		BytesMapPersist entityMapPersist = bytesMapPersists.get(mapName);
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
	
	public static class Builder implements EzyBuilder<BytesMapPersistFactory> {
		protected NameIdMapPersist mapNameIdMapPersist;
		protected NameIdMapPersist atomicLongNameIdMapPersist;
		protected NameIdMapPersist messageChannelNameIdMapPersist;
		protected Map<String, BytesMapPersist> bytesMapPersists;
		
		public Builder() {
			this.bytesMapPersists = new HashMap<>();
		}
		
		public void addMapPersist(String mapName, BytesMapPersist entityMapPersist) {
			this.bytesMapPersists.put(mapName, entityMapPersist);
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
		public BytesMapPersistFactory build() {
			if(mapNameIdMapPersist == null)
				mapNameIdMapPersist = new SimpleNameIdMapPersist();
			if(atomicLongNameIdMapPersist == null)
				atomicLongNameIdMapPersist = new SimpleNameIdMapPersist();
			if(messageChannelNameIdMapPersist == null)
				messageChannelNameIdMapPersist = new SimpleNameIdMapPersist();
			return new SimpleBytesMapPersistFactory(this);
		}
	}
	
}
