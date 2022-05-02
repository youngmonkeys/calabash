package com.tvd12.calabash.core.setting;

public interface ISettings {

    int DEFAULT_MAP_EVICTION_INTERVAL = 3;

    String DEFAULT_ATOMIC_LONG_MAP_NAME = "___atomic_long___";

    String getAtomicLongMapName();
}
