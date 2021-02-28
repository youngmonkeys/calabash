package com.tvd12.calabash.client;

public interface MessageChannelSubscriber {
	
	void onMessage(byte[] channel, byte[] messageBytes);
	
}
