package com.tvd12.calabash.server.core.setting;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SimpleMapEvictionSetting implements MapEvictionSetting {
	
	protected int keyMaxIdleTime;
	
}
