package com.tvd12.calabash.core.setting;

public interface IMapSettings {

    int getMapEvictionInterval();

    Object getMapSetting(String mapName);
}
