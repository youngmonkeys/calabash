package com.tvd12.calabash.persist.util;

import com.tvd12.calabash.persist.annotation.MapPersistence;

import static com.tvd12.ezyfox.io.EzyStrings.isNoContent;
import static com.tvd12.ezyfox.reflect.EzyClasses.getVariableName;

public final class MapPersistenceAnnotations {

    private MapPersistenceAnnotations() {}

    public static String getMapName(Object mapPersistence) {
        return getMapName(mapPersistence.getClass());
    }

    public static String getMapName(Class<?> clazz) {
        MapPersistence anno = clazz.getAnnotation(MapPersistence.class);
        String name = anno.value();
        return isNoContent(name) ? getVariableName(clazz) : name;
    }
}
