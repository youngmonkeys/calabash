package com.tvd12.calabash.client.setting;

public interface MessageChannelSetting {

	Class<?> getMessageType();
	
	int getSubThreadPoolSize();
	
}
