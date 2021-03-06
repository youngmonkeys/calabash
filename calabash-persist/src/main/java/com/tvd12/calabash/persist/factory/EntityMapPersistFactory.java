package com.tvd12.calabash.persist.factory;

import com.tvd12.calabash.persist.EntityMapPersist;
import com.tvd12.calabash.persist.NameIdMapPersist;

public interface EntityMapPersistFactory {

	EntityMapPersist<?, ?> newMapPersist(String mapName);
	
	NameIdMapPersist newMapNameIdMapPersist();
	
	NameIdMapPersist newAtomicLongNameIdMapPersist();
	
	NameIdMapPersist newMessageChannelNameIdMapPersist();
}
