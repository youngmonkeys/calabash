package com.tvd12.calabash.server;

import com.tvd12.ezyfox.util.EzyLoggable;
import com.tvd12.ezyfox.util.EzyStartable;
import com.tvd12.ezyfox.util.EzyStoppable;
import com.tvd12.quick.rpc.server.QuickRpcServer;
import com.tvd12.quick.rpc.server.setting.QuickRpcSettings;

public class CalabashServer 
		extends EzyLoggable 
		implements EzyStartable, EzyStoppable {
	
	protected QuickRpcServer rpcServer;
	
	@Override
	public void start() throws Exception {
		QuickRpcSettings settings = QuickRpcSettings.builder()
				.username("admin")
				.password("admin")
				.build();
		rpcServer = new QuickRpcServer(settings)
				.scan("com.tvd12.calabash.rpc.common")
				.scan("com.tvd12.calabash.server.controller");
		rpcServer.start();
	}
	
	@Override
	public void stop() {
		if(rpcServer != null)
			rpcServer.stop();
	}
	
}
