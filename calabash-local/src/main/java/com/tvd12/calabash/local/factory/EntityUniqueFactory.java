package com.tvd12.calabash.local.factory;

import java.util.Map;
import java.util.function.Function;

public interface EntityUniqueFactory {

	<V> Map<Object, Function<V, Object>> newUniqueKeyMaps(String mapName);
	
}
