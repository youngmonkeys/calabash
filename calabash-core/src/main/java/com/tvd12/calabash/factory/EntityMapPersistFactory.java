package com.tvd12.calabash.factory;

import com.tvd12.calabash.core.EntityMapPersist;

public interface EntityMapPersistFactory {

	EntityMapPersist<?, ?> newMapPersist(String mapName);
	
}
