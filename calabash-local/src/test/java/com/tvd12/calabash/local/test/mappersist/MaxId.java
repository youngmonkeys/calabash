package com.tvd12.calabash.local.test.mappersist;

import com.tvd12.calabash.core.setting.ISettings;
import com.tvd12.ezydata.database.annotation.EzyCollection;
import com.tvd12.ezyfox.annotation.EzyId;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EzyCollection(ISettings.DEFAULT_ATOMIC_LONG_MAP_NAME)
public class MaxId {

	@EzyId
	protected String id;
	protected long value;
	
}
