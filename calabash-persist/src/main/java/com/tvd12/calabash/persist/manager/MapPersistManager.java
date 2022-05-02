package com.tvd12.calabash.persist.manager;

public interface MapPersistManager {

    <T> T getMapPersist(String mapName);

    void addMapPersist(String mapName, Object mapPersist);

    default boolean hasMapPersist(String mapName) {
        return getMapPersist(mapName) != null;
    }
}
