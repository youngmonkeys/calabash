package com.tvd12.calabash.backup.action;

import com.tvd12.ezyfox.constant.EzyConstant;
import com.tvd12.ezyfox.util.EzyEnums;
import lombok.Getter;

import java.util.Map;

public enum BackupActionType implements EzyConstant {

    BACKUP_ONE(1),
    BACKUP_MANY(2),
    REMOVE_ONE(3),
    REMOVE_MANY(4),
    CLEAR(3);

    private static final Map<Integer, BackupActionType> MAP = EzyEnums.enumMapInt(BackupActionType.class);
    @Getter
    private final int id;

    private BackupActionType(int id) {
        this.id = id;
    }

    public static BackupActionType valueOf(int id) {
        BackupActionType answer = MAP.get(id);
        if (answer == null) {
            throw new IllegalArgumentException("has no enum with id: " + id);
        }
        return answer;
    }

    @Override
    public String getName() {
        return toString();
    }
}
