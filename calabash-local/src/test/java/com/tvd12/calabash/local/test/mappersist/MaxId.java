package com.tvd12.calabash.local.test.mappersist;

import com.tvd12.calabash.core.setting.ISettings;

import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity(ISettings.DEFAULT_ATOMIC_LONG_MAP_NAME)
public class MaxId {

	@Id
	protected String id;
	protected long value;
	
}
