package com.tvd12.calabash.backup.setting;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SimpleMapBackupSetting implements MapBackupSetting {
	
	protected long delay = 1000L;
	protected boolean async = true;
	
}
