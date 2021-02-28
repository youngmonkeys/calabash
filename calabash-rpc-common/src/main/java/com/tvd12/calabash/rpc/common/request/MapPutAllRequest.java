package com.tvd12.calabash.rpc.common.request;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MapPutAllRequest {

	protected int mapId;
	protected Map<byte[], byte[]> valueMap;
	
}
