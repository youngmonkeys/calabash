package com.tvd12.calabash.client.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.tvd12.calabash.client.CalabashClientProxy;
import com.tvd12.calabash.client.setting.EntityMapSetting;
import com.tvd12.calabash.core.EntityMap;
import com.tvd12.ezyfox.builder.EzyBuilder;
import com.tvd12.ezyfox.codec.EzyEntityCodec;
import com.tvd12.ezyfox.constant.EzyHasIntId;

@SuppressWarnings({"rawtypes", "unchecked"})
public class EntityMapImpl<K, V> implements EntityMap<K, V>, EzyHasIntId {

	protected final int mapId;
	protected final String mapName;
	protected final Class<K> keyType;
	protected final Class<V> valueType;
	protected final CalabashClientProxy clientProxy;
	protected final EzyEntityCodec entityCodec;
	protected final EntityMapSetting setting;
	
	protected EntityMapImpl(Builder builder) {
		this.setting = builder.setting;
		this.mapName = builder.mapName;
		this.keyType = setting.getKeyType();
		this.valueType = setting.getValueType();
		this.clientProxy = builder.clientProxy;
		this.entityCodec = builder.entityCodec;
		this.mapId = clientProxy.mapGetId(mapName);
	}
	
	@Override
	public Map<K, V> loadAll() {
		Map<byte[], byte[]> keyValueBytesMap = clientProxy.mapGetAll(mapId);
		Map<K, V> answer = new HashMap<>();
		for(byte[] keyBytes : keyValueBytesMap.keySet()) {
			byte[] valueBytes = keyValueBytesMap.get(keyBytes);
			K key = entityCodec.deserialize(keyBytes, keyType);
			V value = entityCodec.deserialize(valueBytes, valueType);
			answer.put(key, value);
		}
		return answer;
	}
	
	@Override
	public void set(K key, V value) {
		byte[] keyBytes = entityCodec.serialize(key);
		byte[] valueBytes = entityCodec.serialize(value);
		clientProxy.mapSet(mapId, keyBytes, valueBytes);
	}
	
	@Override
	public V put(K key, V value) {
		byte[] keyBytes = entityCodec.serialize(key);
		byte[] valueBytes = entityCodec.serialize(value);
		byte[] oldValue = clientProxy.mapPut(mapId, keyBytes, valueBytes);
		if(oldValue == null)
			return null;
		return entityCodec.deserialize(oldValue, valueType);
	}
	
	@Override
	public void putAll(Map<? extends K, ? extends V> m) {
		Map<byte[], byte[]> keyValueBytesMap = new HashMap<>();
		for(K key : m.keySet()) {
			V value = m.get(key);
			byte[] keyBytes = entityCodec.serialize(key);
			byte[] valueBytes = entityCodec.serialize(value);
			keyValueBytesMap.put(keyBytes, valueBytes);
		}
		clientProxy.mapPutAll(mapId, keyValueBytesMap);
	}
	
	@Override
	public V get(Object key) {
		byte[] keyBytes = entityCodec.serialize(key);
		byte[] valueBytes = clientProxy.mapGet(mapId, keyBytes);
		if(valueBytes == null)
			return null;
		V value = entityCodec.deserialize(valueBytes, valueType);
		return value;
	}
	
	public Map<K, V> get(Collection<K> keys) {
		byte[][] keyBytesArray = new byte[keys.size()][];
		int i = 0;
		for(K key : keys)
			keyBytesArray[i++] = entityCodec.serialize(key);
		List<byte[]> valueBytesList = clientProxy.mapGet(mapId, keyBytesArray);
		Map<K, V> answer = new HashMap<>();
		int k = 0;
		for(K key : keys) {
			byte[] valueBytes = valueBytesList.get(k ++);
			if(valueBytes == null)
				continue;
			V value = entityCodec.deserialize(valueBytes, valueType);
			answer.put(key, value);
		}
		return answer;
	}
	
	@Override
	public V getByQuery(K key, Object query) {
		byte[] keyBytes = entityCodec.serialize(key);
		byte[] queryBytes = entityCodec.serialize(query);
		byte[] valueBytes = clientProxy.mapGetByQuery(mapId, keyBytes, queryBytes);
		if(valueBytes == null)
			return null;
		V value = entityCodec.deserialize(valueBytes, valueType);
		return value;
	}
	
	@Override
	public boolean containsKey(Object key) {
		byte[] keyBytes = entityCodec.serialize(key);
		return clientProxy.mapContainsKey(mapId, keyBytes);
	}

	@Override
	public boolean containsValue(Object value) {
		byte[] valueBytes = entityCodec.serialize(value);
		return clientProxy.mapContainsValue(mapId, valueBytes);
	}

	@Override
	public V remove(Object key) {
		byte[] keyBytes = entityCodec.serialize(key);
		byte[] valueBytes = clientProxy.mapRemove(mapId, keyBytes);
		if(valueBytes == null)
			return null;
		V value = entityCodec.deserialize(valueBytes, valueType);
		return value; 
	}
	
	@Override
	public void remove(Set<K> keys) {
		byte[][] keyBytesArray = new byte[keys.size()][];
		int i = 0;
		for(K key : keys)
			keyBytesArray[i++] = entityCodec.serialize(key);
		clientProxy.mapRemove(mapId, keyBytesArray);
	}
	

	@Override
	public Set<K> keySet() {
		Set<byte[]> keyBytesSet = clientProxy.mapKeySet(mapId);
		Set<K> answer = new HashSet<>();
		for(byte[] keyBytes : keyBytesSet) {
			K key = entityCodec.deserialize(keyBytes, keyType);
			answer.add(key);
		}
		return answer;
	}

	@Override
	public Collection<V> values() {
		List<byte[]> valueBytesList = clientProxy.mapValues(mapId);
		List<V> answer = new ArrayList<>();
		for(byte[] valueBytes : valueBytesList) {
			V value = entityCodec.deserialize(valueBytes, valueType);
			answer.add(value);
		}
		return answer;
	}

	@Override
	public Set<Entry<K, V>> entrySet() {
		return loadAll().entrySet();
	}
	
	@Override
	public int size() {
		return (int)sizeLong();
	}
	
	public long sizeLong() {
		long size = clientProxy.mapSize(mapId);
		return size;
	}

	@Override
	public boolean isEmpty() {
		return size() == 0;
	}
	
	@Override
	public void clear() {
		clientProxy.mapClear(mapId);
	}
	
	@Override
	public int getId() {
		return mapId;
	}
	
	@Override
	public String getName() {
		return mapName;
	}
	
	@Override
	public String toString() {
		return "Map(" + mapId + ", " + mapName + ")";
	}
	
	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder implements EzyBuilder<EntityMapImpl> {

		protected String mapName;
		protected EzyEntityCodec entityCodec;
		protected EntityMapSetting setting;
		protected CalabashClientProxy clientProxy;
		
		public Builder mapName(String mapName) {
			this.mapName = mapName;
			return this;
		}
		
		public Builder setting(EntityMapSetting setting) {
			this.setting = setting;
			return this;
		}
		
		public Builder entityCodec(EzyEntityCodec entityCodec) {
			this.entityCodec = entityCodec;
			return this;
		}
		
		public Builder clientProxy(CalabashClientProxy clientProxy) {
			this.clientProxy = clientProxy;
			return this;
		}
		
		@Override
		public EntityMapImpl build() {
			return new EntityMapImpl<>(this);
		}
		
	}

}
