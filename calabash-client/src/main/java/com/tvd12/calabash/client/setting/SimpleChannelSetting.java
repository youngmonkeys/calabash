package com.tvd12.calabash.client.setting;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SimpleChannelSetting implements MessageChannelSetting {

	protected Class<?> messageType;
	protected int subThreadPoolSize = 1;

}
