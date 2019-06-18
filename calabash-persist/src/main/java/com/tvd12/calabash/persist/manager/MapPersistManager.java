package com.tvd12.calabash.persist.manager;

public interface MapPersistManager {

	<T> T getMapPersist(String mapName);
	
	void addMapPersist(String mapName, Object mapPersist);
	
}
