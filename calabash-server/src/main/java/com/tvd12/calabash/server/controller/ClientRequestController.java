package com.tvd12.calabash.server.controller;

import com.tvd12.calabash.core.BytesMap;
import com.tvd12.calabash.rpc.common.Commands;
import com.tvd12.calabash.rpc.common.request.MapGetIdRequest;
import com.tvd12.calabash.rpc.common.request.MapGetOneRequest;
import com.tvd12.calabash.rpc.common.request.MapSetRequest;
import com.tvd12.calabash.server.core.CalabashServerContext;
import com.tvd12.ezyfox.bean.annotation.EzyAutoBind;
import com.tvd12.quick.rpc.server.annotation.Rpc;
import com.tvd12.quick.rpc.server.annotation.RpcController;

import lombok.Setter;

@Setter
@RpcController
public class ClientRequestController {

	@EzyAutoBind
	private CalabashServerContext serverContext;
	
	@Rpc(Commands.MAP_GET_ID_VALUE)
	public int mapGetId(MapGetIdRequest request) {
		return serverContext.getMapId(request.getMapName());
	}
	
	@Rpc(Commands.MAP_SET)
	public boolean mapSet(MapSetRequest request) {
		String mapName = serverContext.getMapName(request.getMapId());
		BytesMap map = serverContext.getBytesMap(mapName);
		map.set(request.getKey(), request.getValue());
		return Boolean.TRUE;
	}
	
	@Rpc(Commands.MAP_GET_ONE)
	public byte[] mapGetOne(MapGetOneRequest request) {
		String mapName = serverContext.getMapName(request.getMapId());
		BytesMap map = serverContext.getBytesMap(mapName);
		return map.get(request.getKey());
	}
	
}
