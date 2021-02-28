package com.tvd12.calabash.rpc.common.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AtomicLongAddAndGetRequest {

	protected int atomicLongId;
	protected long delta;
	
}
