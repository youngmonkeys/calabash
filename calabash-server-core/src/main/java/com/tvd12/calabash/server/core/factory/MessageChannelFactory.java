package com.tvd12.calabash.server.core.factory;

import com.tvd12.calabash.server.core.message.MessageChannel;

public interface MessageChannelFactory {

    MessageChannel newChannel(String name);
}
