package com.tvd12.calabash.backend.setting;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SimpleMapSetting implements MapSetting {

	protected String mapName;
	protected SimpleMapBackupSetting backupSetting = new SimpleMapBackupSetting();
	protected SimpleMapPersistSetting persistSetting = new SimpleMapPersistSetting();
}
