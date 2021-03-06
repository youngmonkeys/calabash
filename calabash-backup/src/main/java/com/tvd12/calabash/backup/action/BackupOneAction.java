package com.tvd12.calabash.backup.action;

import com.tvd12.calabash.core.util.ByteArray;

import lombok.Getter;

@Getter
public class BackupOneAction implements BackupAction {

	protected final ByteArray key;
	protected final byte[] value;
	
	public BackupOneAction(ByteArray key, byte[] value) {
		this.key = key;
		this.value = value;
	}
	
	@Override
	public BackupActionType getAction() {
		return BackupActionType.BACKUP_ONE;
	}

}
