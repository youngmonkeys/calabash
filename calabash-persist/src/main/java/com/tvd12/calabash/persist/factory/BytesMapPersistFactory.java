package com.tvd12.calabash.persist.factory;

import com.tvd12.calabash.persist.BytesMapPersist;
import com.tvd12.calabash.persist.NameIdMapPersist;

public interface BytesMapPersistFactory {

    BytesMapPersist newMapPersist(String mapName);

    NameIdMapPersist newMapNameIdMapPersist();

    NameIdMapPersist newAtomicLongNameIdMapPersist();

    NameIdMapPersist newMessageChannelNameIdMapPersist();
}
