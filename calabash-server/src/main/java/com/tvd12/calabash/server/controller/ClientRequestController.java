package com.tvd12.calabash.server.controller;

import com.tvd12.calabash.rpc.common.Commands;
import com.tvd12.calabash.rpc.common.request.MapGetIdRequest;
import com.tvd12.quick.rpc.server.annotation.Rpc;
import com.tvd12.quick.rpc.server.annotation.RpcController;

@RpcController
public class ClientRequestController {

	@Rpc(Commands.MAP_GET_ID_VALUE)
	public int greet(MapGetIdRequest request) {
		return 1;
	}
	
}
