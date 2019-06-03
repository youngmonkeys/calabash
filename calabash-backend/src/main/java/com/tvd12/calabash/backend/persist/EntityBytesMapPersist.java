package com.tvd12.calabash.backend.persist;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.tvd12.calabash.backend.BytesMapPersist;
import com.tvd12.calabash.backend.MapPersist;
import com.tvd12.calabash.core.util.ByteArray;
import com.tvd12.ezyfox.builder.EzyBuilder;
import com.tvd12.ezyfox.codec.EzyEntityCodec;
import com.tvd12.ezyfox.util.EzyLoggable;

@SuppressWarnings({"rawtypes", "unchecked"})
public class EntityBytesMapPersist extends EzyLoggable implements BytesMapPersist {

	protected final Class<?> keyType;
	protected final Class<?> valueType;
	protected final MapPersist mapPersist;
	protected final EzyEntityCodec entityCodec;
	
	protected EntityBytesMapPersist(Builder builder) {
		this.mapPersist = builder.mapPersist;
		this.entityCodec = builder.entityCodec;
		this.keyType = mapPersist.getKeyType();
		this.valueType = mapPersist.getValueType();
	}
	
	@Override
	public Map<ByteArray, byte[]> loadAll() {
		Map entities = mapPersist.loadAll();
		return serializeEntities(entities);
	}

	@Override
	public Map<ByteArray, byte[]> load(Set<ByteArray> keys) {
		Map entities = mapPersist.load(keys);
		return serializeEntities(entities);
	}
	
	protected Map<ByteArray, byte[]> serializeEntities(Map entities) {
		Map<ByteArray, byte[]> answer = new HashMap<>();
		for(Object keyEntity : entities.keySet()) {
			Object valueEntity = entities.get(keyEntity);
			byte[] key = entityCodec.serialize(keyEntity);
			byte[] value = entityCodec.serialize(valueEntity);
			answer.put(new ByteArray(key), value);
		}
		return answer;
	}

	@Override
	public byte[] load(ByteArray key) {
		Object keyEntity = entityCodec.deserialize(key.getBytes(), keyType);
		Object valueEntity = mapPersist.load(keyEntity);
		if(valueEntity == null)
			return null;
		byte[] value = entityCodec.serialize(valueEntity);
		return value;
	}

	@Override
	public void persist(ByteArray key, byte[] value) {
		Object keyEntity = entityCodec.deserialize(key.getBytes(), keyType);
		Object valueEntity = entityCodec.deserialize(value, valueType);
		mapPersist.persist(keyEntity, valueEntity);
	}

	@Override
	public void persist(Map<ByteArray, byte[]> m) {
		Map entities = new HashMap<>();
		for(ByteArray key : m.keySet()) {
			byte[] value = m.get(key);
			Object keyEntity = entityCodec.deserialize(key.getBytes(), keyType);
			Object valueEntity = entityCodec.deserialize(value, valueType);
			entities.put(keyEntity, valueEntity);
		}
		mapPersist.persist(entities);
	}

	@Override
	public void delete(ByteArray key) {
		Object keyEntity = entityCodec.deserialize(key.getBytes(), keyType);
		mapPersist.delete(keyEntity);
	}

	@Override
	public void delete(Set<ByteArray> keys) {
		Set keyEntities = new HashSet<>();
		for(ByteArray key : keys) {
			Object keyEntity = entityCodec.deserialize(key.getBytes(), keyType);
			keyEntities.add(keyEntity);
		}
		mapPersist.delete(keyEntities);
	}
	
	public static Builder builder() {
		return new Builder();
	}

	public static class Builder implements EzyBuilder<EntityBytesMapPersist> {
		protected MapPersist mapPersist;
		protected EzyEntityCodec entityCodec;
		
		public Builder mapPersist(MapPersist mapPersist) {
			this.mapPersist = mapPersist;
			return this;
		}
		
		public Builder entityCodec(EzyEntityCodec entityCodec) {
			this.entityCodec = entityCodec;
			return this;
		}
		
		@Override
		public EntityBytesMapPersist build() {
			return new EntityBytesMapPersist(this);
		}
	}
}
