package com.tvd12.calabash.rpc.common.response;

import com.tvd12.ezyfox.binding.annotation.EzyArrayBinding;
import com.tvd12.quick.rpc.core.annotation.RpcResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RpcResponse
@EzyArrayBinding
@AllArgsConstructor
@NoArgsConstructor
public class MessageResponse {
    protected int channelId;
    protected byte[] message;
}
