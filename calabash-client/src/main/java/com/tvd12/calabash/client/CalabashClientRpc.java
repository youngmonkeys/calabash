package com.tvd12.calabash.client;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.tvd12.calabash.client.exception.CalabaseRpcException;
import com.tvd12.calabash.rpc.common.Command;
import com.tvd12.calabash.rpc.common.request.AtomicLongAddAndGetRequest;
import com.tvd12.calabash.rpc.common.request.AtomicLongGetIdRequest;
import com.tvd12.calabash.rpc.common.request.AtomicLongGetRequest;
import com.tvd12.calabash.rpc.common.request.AtomicLongIncrementAndGetRequest;
import com.tvd12.calabash.rpc.common.request.ChannelGetIdRequest;
import com.tvd12.calabash.rpc.common.request.MapClearRequest;
import com.tvd12.calabash.rpc.common.request.MapContainsKeyRequest;
import com.tvd12.calabash.rpc.common.request.MapGetByQueryRequest;
import com.tvd12.calabash.rpc.common.request.MapGetIdRequest;
import com.tvd12.calabash.rpc.common.request.MapGetManyRequest;
import com.tvd12.calabash.rpc.common.request.MapGetOneRequest;
import com.tvd12.calabash.rpc.common.request.MapPutAllRequest;
import com.tvd12.calabash.rpc.common.request.MapPutRequest;
import com.tvd12.calabash.rpc.common.request.MapRemoveManyRequest;
import com.tvd12.calabash.rpc.common.request.MapRemoveOneRequest;
import com.tvd12.calabash.rpc.common.request.MapSetRequest;
import com.tvd12.calabash.rpc.common.request.PublishRequest;
import com.tvd12.calabash.rpc.common.request.SubscribeRequest;
import com.tvd12.ezyfox.util.EzyLoggable;
import com.tvd12.quick.rpc.client.QuickRpcClient;
import com.tvd12.quick.rpc.client.entity.RpcRequest;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CalabashClientRpc 
		extends EzyLoggable 
		implements CalabashClientProxy {

	protected final QuickRpcClient client;
	
	@Override
	public int mapGetId(String mapName) {
		return call(Command.MAP_GET_ID, new MapGetIdRequest(mapName), int.class);
	}

	@Override
	public void mapSet(int mapId, byte[] key, byte[] value) {
		MapSetRequest request = new MapSetRequest(mapId, key, value);
		call(Command.MAP_SET, request, boolean.class);
	}

	@Override
	public byte[] mapPut(int mapId, byte[] key, byte[] value) {
		MapPutRequest request = new MapPutRequest(mapId, key, value);
		return call(Command.MAP_PUT, request, byte[].class);
	}

	@Override
	public void mapPutAll(int mapId, Map<byte[], byte[]> m) {
		MapPutAllRequest request = new MapPutAllRequest(mapId, m);
		call(Command.MAP_PUT_ALL, request, boolean.class);
	}

	@Override
	public byte[] mapGet(int mapId, byte[] key) {
		MapGetOneRequest request = new MapGetOneRequest(mapId, key);
		return call(Command.MAP_GET_ONE, request, byte[].class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<byte[]> mapGet(int mapId, byte[]... keys) {
		MapGetManyRequest request = new MapGetManyRequest(mapId, keys);
		return call(Command.MAP_GET_MANY, request, List.class);
	}

	@Override
	public byte[] mapGetByQuery(int mapId, byte[] key, byte[] query) {
		MapGetByQueryRequest request = new MapGetByQueryRequest(mapId, key, query);
		return call(Command.MAP_GET_BY_QUERY, request, byte[].class);
	}

	@Override
	public boolean mapContainsKey(int mapId, byte[] key) {
		MapContainsKeyRequest request = new MapContainsKeyRequest(mapId, key);
		return call(Command.MAP_CONTAINS_KEY, request, boolean.class);
	}

	@Override
	public boolean mapContainsValue(int mapId, byte[] value) {
		throw new UnsupportedOperationException("unsupported");
	}

	@Override
	public byte[] mapRemove(int mapId, byte[] key) {
		MapRemoveOneRequest request = new MapRemoveOneRequest(mapId, key);
		return call(Command.MAP_REMOVE_ONE, request, byte[].class);
	}

	@Override
	public void mapRemove(int mapId, byte[]... keys) {
		MapRemoveManyRequest request = new MapRemoveManyRequest(mapId, keys);
		call(Command.MAP_REMOVE_MANY, request, boolean.class);
	}

	@Override
	public Set<byte[]> mapKeySet(int mapId) {
		throw new UnsupportedOperationException("unsupported");
	}

	@Override
	public List<byte[]> mapValues(int mapId) {
		throw new UnsupportedOperationException("unsupported");
	}

	@Override
	public Map<byte[], byte[]> mapGetAll(int mapId) {
		throw new UnsupportedOperationException("unsupported");
	}

	@Override
	public void mapClear(int mapId) {
		MapClearRequest request = new MapClearRequest(mapId);
		call(Command.MAP_CLEAR, request, boolean.class);
	}

	@Override
	public long mapSize(int mapId) {
		throw new UnsupportedOperationException("unsupported");
	}

	@Override
	public int atomicLongGetId(String atomicLongName) {
		AtomicLongGetIdRequest request = new AtomicLongGetIdRequest(atomicLongName);
		return call(Command.ATOMIC_LONG_GET_ID, request, int.class);
	}

	@Override
	public long atomicLongGet(int atomicLongId) {
		AtomicLongGetRequest request = new AtomicLongGetRequest(atomicLongId);
		return call(Command.ATOMIC_LONG_GET, request, long.class);
	}

	@Override
	public long atomicLongIncrementAndGet(int atomicLongId) {
		AtomicLongIncrementAndGetRequest request = new AtomicLongIncrementAndGetRequest(atomicLongId);
		return call(Command.ATOMIC_LONG_INCREMENT_AND_GET, request, long.class);
	}

	@Override
	public long atomicLongAddAndGet(int atomicLongId, long delta) {
		AtomicLongAddAndGetRequest request = new AtomicLongAddAndGetRequest(atomicLongId, delta);
		return call(Command.ATOMIC_LONG_ADD_AND_GET, request, long.class);
	}

	@Override
	public int channelGetId(String channelName) {
		ChannelGetIdRequest request = new ChannelGetIdRequest(channelName);
		return call(Command.CHANNEL_GET_ID, request, int.class);
	}

	@Override
	public void publish(int channelId, byte[] message) {
		PublishRequest request = new PublishRequest(channelId, message);
		client.fire(new RpcRequest(Command.PUBLISH.getValue(), request));
	}

	@Override
	public void subscribe(int channelId, MessageChannelSubscriber subscriber) {
		SubscribeRequest request = new SubscribeRequest(channelId);
		call(Command.SUBSCRIBE, request, boolean.class);
	}

	@Override
	public void close() {
		client.close();
		
	}
	
	private <T> T call(Command cmd, Object request, Class<T> responseType) {
		try {
			return client.call(
				new RpcRequest(cmd.getValue(), request), 
				responseType
			);
		}
		catch (Exception e) {
			throw new CalabaseRpcException("rpc command: " + cmd + " failed, request: " + request, e);
		}
	}

}
