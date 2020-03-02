package com.tvd12.calabash.local.test.mappersist;

import java.util.Map;

import com.tvd12.calabash.core.EntityMapPersist;
import com.tvd12.calabash.core.annotation.MapPersistence;
import com.tvd12.calabash.core.setting.ISettings;
import com.tvd12.ezyfox.bean.annotation.EzyAutoBind;
import com.tvd12.ezyfox.bean.annotation.EzySingleton;

import lombok.Setter;

@Setter
@EzySingleton
@MapPersistence(ISettings.DEFAULT_ATOMIC_LONG_MAP_NAME)
public class MaxIdMapPersist implements EntityMapPersist<String, Long> {

	@EzyAutoBind
	protected MaxIdRepo maxIdRepo;
	
	@Override
	public Long load(String key) {
		MaxId entity = maxIdRepo.findById(key);
		if(entity == null)
			return null;
		return entity.getValue();
	}
	
	@Override
	public void persist(String key, Long value) {
		maxIdRepo.save(new MaxId(key, value));
	}

	@Override
	public void persist(Map<String, Long> m) {
		for(String key : m.keySet())
			persist(key, m.get(key));
	}

}
