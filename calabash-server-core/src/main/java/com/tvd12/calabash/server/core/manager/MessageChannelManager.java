package com.tvd12.calabash.server.core.manager;

import java.util.List;

import com.tvd12.calabash.server.core.message.MessageChannel;

public interface MessageChannelManager {

	MessageChannel getChannel(String name);
	
	List<MessageChannel> getChannels();

}
