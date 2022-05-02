package com.tvd12.calabash.local.setting;

import com.tvd12.calabash.core.setting.IMapSetting;

public interface EntityMapSetting extends IMapSetting {

    @Override
    EntityMapPersistSetting getPersistSetting();

    @Override
    EntityMapEvictionSetting getEvictionSetting();
}
