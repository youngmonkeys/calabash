package com.tvd12.calabash;

import com.tvd12.calabash.core.EntityMap;

public interface CalabashEntity {

    default <K, V> EntityMap<K, V> getEntityMap(String name) {
        throw new UnsupportedOperationException();
    }
}
