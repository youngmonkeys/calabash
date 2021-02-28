package com.tvd12.calabash.rpc.common.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MapPutRequest {

	protected int mapId;
	protected byte[] key;
	protected byte[] value;
	
}
