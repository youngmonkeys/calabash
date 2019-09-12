package com.tvd12.calabash.backend.backup;

import java.util.Map;

import com.tvd12.ezyfox.constant.EzyConstant;
import com.tvd12.ezyfox.util.EzyEnums;

import lombok.Getter;

public enum BackupActionType implements EzyConstant {

	BACKUP_ONE(1),
	BACKUP_MANY(2),
	REMOVE_ONE(3),
	REMOVE_MANY(4),
	CLEAR(3);
	
	@Getter
	private final int id;
	private static final Map<Integer, BackupActionType> MAP = EzyEnums.enumMapInt(BackupActionType.class);
	
	private BackupActionType(int id) {
		this.id = id;
	}

	@Override
	public String getName() {
		return toString();
	}
	
	public static BackupActionType valueOf(int id) {
		BackupActionType answer = MAP.get(id);
		if(answer == null)
			throw new IllegalArgumentException("has no enum with id: " + id);
		return answer;
	}
}
