package com.tvd12.calabash.backend.factory;

import com.tvd12.calabash.backend.MapPersist;

public interface MapPersistFactory {

	MapPersist<?, ?> newMapPersist(String mapName);
	
}
