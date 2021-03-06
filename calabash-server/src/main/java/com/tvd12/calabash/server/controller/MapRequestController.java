package com.tvd12.calabash.server.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.tvd12.calabash.core.BytesMap;
import com.tvd12.calabash.core.util.ByteArray;
import com.tvd12.calabash.rpc.common.Commands;
import com.tvd12.calabash.rpc.common.ErrorCodes;
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
import com.tvd12.calabash.server.core.CalabashServerContext;
import com.tvd12.ezyfox.bean.annotation.EzyAutoBind;
import com.tvd12.ezyfox.exception.BadRequestException;
import com.tvd12.quick.rpc.server.annotation.Rpc;
import com.tvd12.quick.rpc.server.annotation.RpcController;

import lombok.Setter;

@Setter
@RpcController
public class MapRequestController {

	@EzyAutoBind
	private CalabashServerContext serverContext;
	
	@Rpc(Commands.MAP_GET_ID_VALUE)
	public int mapGetId(MapGetIdRequest request) {
		return serverContext.getMapId(request.getMapName());
	}
	
	@Rpc(Commands.MAP_SET)
	public boolean mapSet(MapSetRequest request) {
		BytesMap map = getBytesMap(request.getMapId());
		map.set(request.getKey(), request.getValue());
		return Boolean.TRUE;
	}
	
	@Rpc(Commands.MAP_PUT)
	public byte[] mapPut(MapPutRequest request) {
		BytesMap map = getBytesMap(request.getMapId());
		return map.put(request.getKey(), request.getValue());
	}
	
	@Rpc(Commands.MAP_PUT_ALL)
	public boolean mapPutAll(MapPutAllRequest request) {
		BytesMap map = getBytesMap(request.getMapId());
		map.setAll(request.getValueMap());
		return Boolean.TRUE;
	}
	
	@Rpc(Commands.MAP_GET_ONE)
	public byte[] mapGetOne(MapGetOneRequest request) {
		BytesMap map = getBytesMap(request.getMapId());
		return map.get(request.getKey());
	}
	
	@Rpc(Commands.MAP_GET_MANY)
	public List<byte[]> mapGetMany(MapGetManyRequest request) {
		BytesMap map = getBytesMap(request.getMapId());
		List<ByteArray> keys = ByteArray.wrap(request.getKeys());
		Map<ByteArray, byte[]> m = map.get(keys);
		List<byte[]> answer = new ArrayList<>(keys.size());
		for(ByteArray key : keys)
			answer.add(m.get(key));
		return answer;
	}
	
	@Rpc(Commands.MAP_GET_BY_QUERY)
	public byte[] mapGetByQuery(MapGetByQueryRequest request) {
		BytesMap map = getBytesMap(request.getMapId());
		return map.getByQuery(request.getKey(), request.getQuery());
	}
	
	@Rpc(Commands.MAP_CONTAINS_KEY)
	public boolean mapGetByQuery(MapContainsKeyRequest request) {
		BytesMap map = getBytesMap(request.getMapId());
		return map.containsKey(request.getKey());
	}
	
	@Rpc(Commands.MAP_REMOVE_ONE)
	public byte[] mapRemoveOne(MapRemoveOneRequest request) {
		BytesMap map = getBytesMap(request.getMapId());
		return map.remove(request.getKey());
	}
	
	@Rpc(Commands.MAP_REMOVE_MANY)
	public boolean mapRemoveMany(MapRemoveManyRequest request) {
		BytesMap map = getBytesMap(request.getMapId());
		map.remove(request.getKeys());
		return Boolean.TRUE;
	}
	
	@Rpc(Commands.MAP_CLEAR)
	public boolean mapClear(MapClearRequest request) {
		BytesMap map = getBytesMap(request.getMapId());
		map.clear();
		return Boolean.TRUE;
	}
	
	private BytesMap getBytesMap(int mapId) {
		BytesMap map = serverContext.getBytesMap(mapId);
		if(map != null)
			return map;
		throw new BadRequestException(ErrorCodes.INVALID_MAP_ID, "there is no map with id: " + mapId);
	}
}
