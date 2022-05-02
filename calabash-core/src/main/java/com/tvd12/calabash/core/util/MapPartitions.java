package com.tvd12.calabash.core.util;

import java.util.*;

public final class MapPartitions {

    private MapPartitions() {}

    public static int getPartitionIndex(int maxPartition, Object key) {
        int hashCode = key.hashCode();
        return Math.abs(hashCode % maxPartition);
    }

    public static <K> Map<Integer, Set<K>> classifyKeys(
        int maxPartition,
        Collection<K> keys
    ) {
        Map<Integer, Set<K>> answer = new HashMap<>();
        for (K key : keys) {
            int index = getPartitionIndex(maxPartition, key);
            answer.computeIfAbsent(index, k -> new HashSet<>())
                .add(key);
        }
        return answer;
    }

    public static <K, V> Map<Integer, Map<K, V>> classifyMaps(
        int maxPartition,
        Map<K, V> map
    ) {
        Map<Integer, Map<K, V>> answer = new HashMap<>();
        for (K key : map.keySet()) {
            int index = getPartitionIndex(maxPartition, key);
            answer.computeIfAbsent(index, k -> new HashMap<>())
                .put(key, map.get(key));
        }
        return answer;
    }
}
