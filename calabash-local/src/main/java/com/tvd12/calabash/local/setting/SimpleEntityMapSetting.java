package com.tvd12.calabash.local.setting;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SimpleEntityMapSetting implements EntityMapSetting {

    protected String mapName;
    protected int maxPartition = 16;
    protected SimpleEntityMapPersistSetting persistSetting
        = new SimpleEntityMapPersistSetting();
    protected SimpleEntityMapEvictionSetting evictionSetting
        = new SimpleEntityMapEvictionSetting();
}
