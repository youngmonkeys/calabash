package com.tvd12.calabash.persist.action;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PersistSaveOneAction implements PersistAction {

    protected final Object key;
    protected final Object value;

    @Override
    public PersistActionType getType() {
        return PersistActionType.SAVE_ONE;
    }
}
