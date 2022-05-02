package com.tvd12.calabash.client.util;

import com.tvd12.calabash.client.annotation.CachedValue;
import com.tvd12.ezyfox.io.EzyStrings;

public final class CachedValueAnnotations {

    private CachedValueAnnotations() {}

    public static String getMapName(Class<?> cachedValueClass) {
        String mapName = getMapName(cachedValueClass.getAnnotation(CachedValue.class));
        if (EzyStrings.isNoContent(mapName)) {
            mapName = cachedValueClass.getSimpleName();
        }
        return mapName;
    }

    public static String getMapName(CachedValue anno) {
        String mapName = anno.value();
        if (EzyStrings.isNoContent(mapName)) {
            mapName = anno.mapName();
        }
        return mapName;
    }
}
