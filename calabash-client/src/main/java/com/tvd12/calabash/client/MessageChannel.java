package com.tvd12.calabash.client;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.function.Consumer;

import com.tvd12.calabash.client.concurrent.CalabashThreadFactory;
import com.tvd12.calabash.client.setting.MessageChannelSetting;
import com.tvd12.ezyfox.builder.EzyBuilder;
import com.tvd12.ezyfox.codec.EzyEntityCodec;

@SuppressWarnings({"rawtypes", "unchecked"})
public class MessageChannel<T> {

	protected final int channelId;
	protected final String channelName;
	protected final Class messageType;
	protected final CalabashClientProxy clientProxy;
	protected final EzyEntityCodec entityCodec;
	protected final MessageChannelSetting setting;
	protected final int subThreadPoolSize;
	protected final String subThreadPoolName;
	protected volatile boolean subscribed;
	protected List<Consumer<T>> subscribers;
	protected ExecutorService subExecutorService;
	
	public MessageChannel(Builder builder) {
		this.setting = builder.setting;
		this.clientProxy = builder.clientProxy;
		this.entityCodec = builder.entityCodec;
		this.channelName = builder.channelName;
		this.messageType = setting.getMessageType();
		this.subThreadPoolSize = setting.getSubThreadPoolSize();
		this.subThreadPoolName = "channel-subscriber-" + channelName;
		this.channelId = clientProxy.channelGetId(channelName);
	}
	
	public void publish(T message) {
		byte[] messageBytes = entityCodec.serialize(message);
		clientProxy.publish(channelId, messageBytes);
	}
	
	public void addSubscriber(Consumer<T> subscriber) {
		synchronized (this) {
			if(!subscribed) {
				this.subscribed = true;
				this.subExecutorService = newSubExecutorService();
				this.subscribers = Collections.synchronizedList(new ArrayList<>());
				this.subscribe();
			}
		}
		this.subscribers.add(subscriber);
	}
	
	protected ExecutorService newSubExecutorService() {
		ThreadFactory threadFactory 
			= CalabashThreadFactory.create(subThreadPoolName);
		ExecutorService executorService 
			= Executors.newFixedThreadPool(subThreadPoolSize, threadFactory);
		Runtime.getRuntime().addShutdownHook(new Thread(() -> executorService.shutdown()));
		return executorService;
	}
	
	protected void subscribe() {
		MessageChannelSubscriber subscriber = new MessageChannelSubscriber() {
			@Override
			public void onMessage(byte[] channel, byte[] messageBytes) {
				T message = (T)entityCodec.deserialize(messageBytes, messageType);
				for(Consumer<T> subscriber : subscribers) {
					subscriber.accept(message);
				}
			}
		};
		for(int i = 0 ; i < subThreadPoolSize ; ++i) {
			subExecutorService.execute(() -> clientProxy.subscribe(channelId, subscriber));
		}
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
