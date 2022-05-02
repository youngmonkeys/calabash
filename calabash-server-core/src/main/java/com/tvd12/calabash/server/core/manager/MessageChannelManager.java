package com.tvd12.calabash.server.core.manager;

import com.tvd12.calabash.server.core.message.MessageChannel;

import java.util.List;

public interface MessageChannelManager {

    MessageChannel getChannel(String name);

    List<MessageChannel> getChannels();
}
