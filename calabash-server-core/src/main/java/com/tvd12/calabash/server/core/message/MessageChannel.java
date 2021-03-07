package com.tvd12.calabash.server.core.message;

public interface MessageChannel {

	void addSubscriber(Object subscriber);
	
	void removeSubscriber(Object subscriber);
	
	void broadcast(Object message);
	
}
