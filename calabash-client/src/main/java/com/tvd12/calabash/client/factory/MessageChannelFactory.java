package com.tvd12.calabash.client.factory;

import com.tvd12.calabash.client.MessageChannel;
import com.tvd12.calabash.client.CalabashClientProxy;
import com.tvd12.calabash.client.setting.MessageChannelSetting;
import com.tvd12.calabash.client.setting.MessageChannelSettingBuilder;
import com.tvd12.calabash.client.setting.Settings;
import com.tvd12.ezyfox.builder.EzyBuilder;
import com.tvd12.ezyfox.codec.EzyEntityCodec;

public class MessageChannelFactory {

	protected final Settings settings;
	protected final EzyEntityCodec entityCodec;
	protected final CalabashClientProxy clientProxy;
	
	protected MessageChannelFactory(Builder builder) {
		this.settings = builder.settings;
		this.clientProxy = builder.clientProxy;
		this.entityCodec = builder.entityCodec;
	}
	
	public <T> MessageChannel<T> newChannel(String name) {
		MessageChannelSetting channelSetting = settings.getChannelSeting(name);
		return newChannel(name, channelSetting);
	}
	
	public <T> MessageChannel<T> newChannel(String name, Class<T> messageType) {
		MessageChannelSetting channelSetting = new MessageChannelSettingBuilder()
				.messageType(messageType)
				.build();
		return newChannel(name, channelSetting);
	}
	
	@SuppressWarnings("unchecked")
	public <T> MessageChannel<T> newChannel(
			String name, MessageChannelSetting channelSetting) {
		if(channelSetting == null)
			throw new IllegalArgumentException("has no setting for channel: " + name);
		return MessageChannel.builder()
				.channelName(name)
				.setting(channelSetting)
				.clientProxy(clientProxy)
				.entityCodec(entityCodec)
				.build();
	}
	
	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder implements EzyBuilder<MessageChannelFactory> {
		
		protected Settings settings;
		protected EzyEntityCodec entityCodec;
		protected CalabashClientProxy clientProxy;
		
		public Builder settings(Settings settings) {
			this.settings = settings;
			return this;
		}
		
		public Builder entityCodec(EzyEntityCodec entityCodec) {
			this.entityCodec = entityCodec;
			return this;
		}
		
		public Builder clientProxy(CalabashClientProxy clientProxy) {
			this.clientProxy = clientProxy;
			return this;
		}
		
		@Override
		public MessageChannelFactory build() {
			return new MessageChannelFactory(this);
		}
		
	}
	
}
