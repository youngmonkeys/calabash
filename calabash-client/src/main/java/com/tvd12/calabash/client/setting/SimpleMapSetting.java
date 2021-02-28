package com.tvd12.calabash.client.setting;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SimpleMapSetting implements EntityMapSetting {

	protected Class<?> keyType;
	protected Class<?> valueType;

}
