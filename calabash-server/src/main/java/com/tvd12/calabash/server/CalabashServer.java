package com.tvd12.calabash.server;

import com.tvd12.calabash.core.util.PropertiesKeeper;
import com.tvd12.calabash.server.core.CalabashServerContext;
import com.tvd12.ezyfox.bean.EzyBeanContext;
import com.tvd12.ezyfox.bean.EzyBeanContextBuilder;
import com.tvd12.ezyfox.util.EzyStartable;
import com.tvd12.ezyfox.util.EzyStoppable;
import com.tvd12.quick.rpc.server.QuickRpcServer;

public class CalabashServer 
		extends PropertiesKeeper<CalabashServer> 
		implements EzyStartable, EzyStoppable {
	
	protected QuickRpcServer rpcServer;
	protected CalabashServerContext serverContext;
	
	@Override
	public void start() throws Exception {
		serverContext = newServerContext();
		rpcServer = newRpcServer();
		rpcServer.start();
	}
	
	@Override
	public void stop() {
		if(rpcServer != null)
			rpcServer.stop();
	}
	
	private CalabashServerContext newServerContext() {
		return CalabashServerContext.builder()
				.build();
	}
	
	private QuickRpcServer newRpcServer() {
		EzyBeanContextBuilder beanContextBuilder = EzyBeanContext.builder()
				.addProperties(properties)
				.addSingleton("serverContext", serverContext);
		return new QuickRpcServer()
				.properties(properties)
				.beanContextBuilder(beanContextBuilder)
				.scan("com.tvd12.calabash.rpc.common")
				.scan("com.tvd12.calabash.server.controller");
	}
}
