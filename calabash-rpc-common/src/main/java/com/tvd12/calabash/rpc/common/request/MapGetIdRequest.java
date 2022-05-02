package com.tvd12.calabash.rpc.common.request;

import com.tvd12.ezyfox.binding.annotation.EzyArrayBinding;
import com.tvd12.quick.rpc.core.annotation.RpcRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RpcRequest
@EzyArrayBinding
@AllArgsConstructor
@NoArgsConstructor
public class MapGetIdRequest {
    protected String mapName;
}
