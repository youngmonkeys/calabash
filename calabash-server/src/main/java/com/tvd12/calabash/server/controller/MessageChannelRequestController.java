package com.tvd12.calabash.server.controller;

import com.tvd12.calabash.rpc.common.Commands;
import com.tvd12.calabash.rpc.common.ErrorCodes;
import com.tvd12.calabash.rpc.common.request.MessageChannelGetIdRequest;
import com.tvd12.calabash.rpc.common.request.MessageChannelPublishRequest;
import com.tvd12.calabash.rpc.common.request.MessageChannelSubscribeRequest;
import com.tvd12.calabash.server.core.CalabashServerContext;
import com.tvd12.calabash.server.core.message.MessageChannel;
import com.tvd12.ezyfox.bean.annotation.EzyAutoBind;
import com.tvd12.ezyfox.exception.BadRequestException;
import com.tvd12.quick.rpc.server.annotation.Rpc;
import com.tvd12.quick.rpc.server.annotation.RpcController;
import com.tvd12.quick.rpc.server.entity.RpcSession;

import lombok.Setter;

@Setter
@RpcController
public class MessageChannelRequestController {

	@EzyAutoBind
	private CalabashServerContext serverContext;
	
	@Rpc(Commands.MESSAGE_CHANNEL_GET_ID)
	public int messageChannelGetId(MessageChannelGetIdRequest request) {
		return serverContext.getMessageChannelId(request.getChannelName());
	}
	
	@Rpc(Commands.MESSSGE_CHANNEL_SUBSCRIBE)
	public boolean messageChannelPublish(
			MessageChannelSubscribeRequest request,
			RpcSession session
	) {
		MessageChannel messageChannel = getMessageChannel(request.getChannelId());
		messageChannel.addSubscriber(session);
		return Boolean.TRUE;
	}
	
	@Rpc(Commands.MESSAGE_CHANNEL_PUBLISH)
	public void messageChannelPublish(MessageChannelPublishRequest request) {
		MessageChannel messageChannel = getMessageChannel(request.getChannelId());
		messageChannel.broadcast(request);
	}

	private MessageChannel getMessageChannel(int channelId) {
		MessageChannel messageChannel = serverContext.getMessageChannel(channelId);
		if(messageChannel != null)
			return messageChannel;
		throw new BadRequestException(ErrorCodes.INVALID_MESSAGE_CHANNEL_ID, "there is no message channel with id: " + channelId);
	}
}
