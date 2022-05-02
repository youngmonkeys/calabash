package com.tvd12.calabash.local.factory;

import com.tvd12.calabash.core.EntityMap;

@SuppressWarnings("rawtypes")
public interface EntityMapFactory {

    EntityMap newMap(String mapName);
}
