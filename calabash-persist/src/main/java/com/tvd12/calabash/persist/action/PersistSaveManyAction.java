package com.tvd12.calabash.persist.action;

import com.tvd12.calabash.core.util.ByteArray;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

@Getter
@AllArgsConstructor
public class PersistSaveManyAction implements PersistAction {

    protected final Map<ByteArray, byte[]> keyValues;

    @Override
    public PersistActionType getType() {
        return PersistActionType.SAVE_MANY;
    }
}
