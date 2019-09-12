package com.tvd12.calabash.core.util;

import static com.tvd12.ezyfox.reflect.EzyClasses.getVariableName;

import com.tvd12.calabash.core.annotation.MapPersistence;
import static com.tvd12.ezyfox.io.EzyStrings.isNoContent;

public final class MapPersistenceAnnotations {

	private MapPersistenceAnnotations() {
	}
	
	public static String getMapName(Object mapPersistence) {
		return getMapName(mapPersistence.getClass());
	}
	
	public static String getMapName(Class<?> clazz) {
		MapPersistence anno = clazz.getAnnotation(MapPersistence.class);
		String name = anno.value();
		return isNoContent(name) ? getVariableName(clazz) : name;
	}
	
}
