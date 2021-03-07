package com.tvd12.calabash.client.manager;

import java.util.HashMap;
import java.util.Map;

import com.tvd12.calabash.client.MessageChannel;
import com.tvd12.calabash.client.factory.MessageChannelFactory;
import com.tvd12.ezyfox.builder.EzyBuilder;

@SuppressWarnings({"rawtypes", "unchecked"})
public class MessageChannelProvider {

	protected final Map<String, MessageChannel> channels;
	protected final MessageChannelFactory channelFactory;
	protected final Map<Integer, String> channelNameById;
	
	protected MessageChannelProvider(Builder builder) {
		this.channels = new HashMap<>();
		this.channelNameById = new HashMap<>();
		this.channelFactory = builder.channelFactory;
	}
	
	public <T> MessageChannel<T> getChannel(int channelId) {
		String name = channelNameById.get(channelId);
		if(name == null)
			return null;
		return channels.get(name);
	}
	
	public <T> MessageChannel<T> getChannel(String name) {
		MessageChannel<T> channel = channels.get(name);
		if(channel == null)
			channel = newChannel(name);
		return channel;
	}
	
	public <T> MessageChannel<T> getChannel(String name, Class<T> messageType) {
		MessageChannel<T> channel = channels.get(name);
		if(channel == null)
			channel = newChannel(name, messageType);
		return channel;
	}
	
	protected <T> MessageChannel<T> newChannel(String name) {
		synchronized (channels) {
			MessageChannel<T> channel = channels.get(name);
			if(channel == null) {
				channel = channelFactory.newChannel(name);
				channels.put(name, channel);
				channelNameById.put(channel.getId(), name);
			}
			return channel;
		}
	}
	
	protected <T> MessageChannel<T> newChannel(String name, Class<T> messageType) {
		synchronized (channels) {
			MessageChannel<T> channel = channels.get(name);
			if(channel == null) {
				channel = channelFactory.newChannel(name, messageType);
				channels.put(name, channel);
				channelNameById.put(channel.getId(), name);
			}
			return channel;
		}
	}
	
	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder implements EzyBuilder<MessageChannelProvider> {
		
		protected MessageChannelFactory channelFactory;
		
		public Builder channelFactory(MessageChannelFactory channelFactory) {
			this.channelFactory = channelFactory;
			return this;
		}
		
		@Override
		public MessageChannelProvider build() {
			return new MessageChannelProvider(this);
		}
		
	}

}
