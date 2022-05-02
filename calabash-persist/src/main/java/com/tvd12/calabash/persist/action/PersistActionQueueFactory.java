package com.tvd12.calabash.persist.action;

import com.tvd12.calabash.core.setting.IMapSettings;

public class PersistActionQueueFactory {

    protected final IMapSettings settings;

    public PersistActionQueueFactory(IMapSettings settings) {
        this.settings = settings;
    }

    public PersistActionQueue newActionQueue(String mapName) {
        return new PersistActionQueue();
    }
}
