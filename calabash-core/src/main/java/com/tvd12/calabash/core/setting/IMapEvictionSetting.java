package com.tvd12.calabash.core.setting;

public interface IMapEvictionSetting {

    int getKeyMaxIdleTime();

    default long getKeyMaxIdleTimeMillis() {
        return getKeyMaxIdleTime() * 1000L;
    }
}
