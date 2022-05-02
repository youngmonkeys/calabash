package com.tvd12.calabash.server.message;

import com.tvd12.calabash.rpc.common.Commands;
import com.tvd12.calabash.server.core.message.MessageChannel;
import com.tvd12.ezyfox.util.EzyLoggable;
import com.tvd12.quick.rpc.server.entity.RpcSession;
import lombok.AllArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
public class MessageChannelImpl extends EzyLoggable implements MessageChannel {

    private final String name;
    private final Map<Object, RpcSession> subscribers = new HashMap<>();

    @Override
    public void addSubscriber(Object subscriber) {
        RpcSession session = (RpcSession) subscriber;
        synchronized (subscriber) {
            subscribers.put(session.getKey(), session);
        }
        logger.debug("channel: {} add subscriber: {}, subscriber count: {}", name, subscriber, subscribers.size());
    }

    @Override
    public void removeSubscriber(Object subscriber) {
        RpcSession session = (RpcSession) subscriber;
        synchronized (subscriber) {
            subscribers.remove(session.getKey());
        }
        logger.debug("channel: {} remove subscriber: {}, subscriber count: {}", name, subscriber, subscribers.size());
    }

    @Override
    public void broadcast(Object message) {
        synchronized (subscribers) {
            for (RpcSession subscriber : subscribers.values()) {
                subscriber.send(Commands.MESSAGE, "", message);
            }
        }
    }

}
