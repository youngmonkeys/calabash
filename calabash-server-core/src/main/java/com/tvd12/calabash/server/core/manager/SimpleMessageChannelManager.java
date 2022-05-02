package com.tvd12.calabash.server.core.manager;

import com.tvd12.calabash.server.core.factory.MessageChannelFactory;
import com.tvd12.calabash.server.core.message.MessageChannel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SimpleMessageChannelManager implements MessageChannelManager {

    protected final MessageChannelFactory factory;
    protected final Map<String, MessageChannel> messageChannels;

    public SimpleMessageChannelManager(MessageChannelFactory factory) {
        this.factory = factory;
        this.messageChannels = new ConcurrentHashMap<>();
    }

    @Override
    public MessageChannel getChannel(String name) {
        MessageChannel channel = messageChannels.get(name);
        if (channel == null) {
            channel = newChannel(name);
        }
        return channel;
    }

    protected MessageChannel newChannel(String name) {
        synchronized (messageChannels) {
            MessageChannel channel = messageChannels.get(name);
            if (channel == null) {
                channel = factory.newChannel(name);
                messageChannels.put(name, channel);
            }
            return channel;
        }
    }

    @Override
    public List<MessageChannel> getChannels() {
        List<MessageChannel> answer = new ArrayList<>(messageChannels.size());
        synchronized (messageChannels) {
            answer.addAll(messageChannels.values());
        }
        return answer;
    }
}
