package com.tvd12.calabash.core.prototype;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface Prototypes {

    static PrototypesBuilder builder() {
        return SimplePrototypes.builder();
    }

    <T> T copy(T origin);

    <V> Set<V> copySet(Collection<V> origin);

    <V> List<V> copyList(Collection<V> origin);

    <K, V> Map<K, V> copyMap(Map<K, V> origin);
}
