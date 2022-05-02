package com.tvd12.calabash.server.core.setting;

import com.tvd12.calabash.backup.setting.MapBackupSetting;
import com.tvd12.calabash.core.setting.IMapSetting;
import com.tvd12.calabash.core.setting.MapEvictionSetting;
import com.tvd12.calabash.persist.setting.MapPersistSetting;

public interface MapSetting extends IMapSetting {

    MapBackupSetting getBackupSetting();

    @Override
    MapPersistSetting getPersistSetting();

    @Override
    MapEvictionSetting getEvictionSetting();
}
