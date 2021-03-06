package com.tvd12.calabash.server.core.setting;

import com.tvd12.calabash.backup.setting.SimpleMapBackupSetting;
import com.tvd12.calabash.core.setting.SimpleMapEvictionSetting;
import com.tvd12.calabash.persist.setting.SimpleMapPersistSetting;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SimpleMapSetting implements MapSetting {

	protected String mapName;
	protected int maxPartition = 16;
	protected SimpleMapBackupSetting backupSetting = new SimpleMapBackupSetting();
	protected SimpleMapPersistSetting persistSetting = new SimpleMapPersistSetting();
	protected SimpleMapEvictionSetting evictionSetting = new SimpleMapEvictionSetting();

}
