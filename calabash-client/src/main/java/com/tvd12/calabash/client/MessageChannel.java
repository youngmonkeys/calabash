package com.tvd12.calabash.client;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

import com.tvd12.calabash.client.setting.MessageChannelSetting;
import com.tvd12.ezyfox.builder.EzyBuilder;
import com.tvd12.ezyfox.codec.EzyEntityCodec;
import com.tvd12.ezyfox.constant.EzyHasIntId;

@SuppressWarnings({"rawtypes", "unchecked"})
public class MessageChannel<T> implements EzyHasIntId {

	protected final int channelId;
	protected final String channelName;
	protected final Class messageType;
	protected final CalabashClientProxy clientProxy;
	protected final EzyEntityCodec entityCodec;
	protected final MessageChannelSetting setting;
	protected volatile boolean subscribed;
	protected List<Consumer<T>> subscribers;
	
	public MessageChannel(Builder builder) {
		this.setting = builder.setting;
		this.clientProxy = builder.clientProxy;
		this.entityCodec = builder.entityCodec;
		this.channelName = builder.channelName;
		this.messageType = setting.getMessageType();
		this.channelId = clientProxy.messageChannelGetId(channelName);
	}
	
	public void publish(T message) {
		byte[] messageBytes = entityCodec.serialize(message);
		clientProxy.messageChannelPublish(channelId, messageBytes);
	}
	
	public void receiveMessage(byte[] message) {
		T msg = (T) entityCodec.deserialize(message, messageType);
		synchronized (this) {
			for(Consumer<T> sub : subscribers)
				sub.accept(msg);
		}
	}
	
	public void addSubscriber(Consumer<T> subscriber) {
		synchronized (this) {
			if(!subscribed) {
				this.subscribed = true;
				this.subscribers = Collections.synchronizedList(new ArrayList<>());
				this.subscribe();
			}
		}
		this.subscribers.add(subscriber);
	}
	
	protected void subscribe() {
		MessageChannelSubscriber subscriber = new MessageChannelSubscriber() {
			@Override
			public void onMessage(byte[] messages) {
				T message = (T)entityCodec.deserialize(messages, messageType);
				for(Consumer<T> subscriber : subscribers) {
					subscriber.accept(message);
				}
			}
		};
		clientProxy.messageChannelSubscribe(channelId, subscriber);
	}
	
	@Override
	public int getId() {
		return channelId;
	}
	
	public String getName() {
		return channelName;
	}
	
	@Override
	public String toString() {
		return "MessageChannel(" + channelId + ", " + channelName + ")";
	}
	
	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder implements EzyBuilder<MessageChannel> {
		protected String channelName;
		protected EzyEntityCodec entityCodec;
		protected MessageChannelSetting setting;
		protected CalabashClientProxy clientProxy;
		
		public Builder channelName(String channelName) {
			this.channelName = channelName;
			return this;
		}
		
		public Builder entityCodec(EzyEntityCodec entityCodec) {
			this.entityCodec = entityCodec;
			return this;
		}
		
		public Builder setting(MessageChannelSetting setting) {
			this.setting = setting;
			return this;
		}
		
		public Builder clientProxy(CalabashClientProxy clientProxy) {
			this.clientProxy = clientProxy;
			return this;
		}
		
		@Override
		public MessageChannel build() {
			return new MessageChannel<>(this);
		}
		
	}
}
