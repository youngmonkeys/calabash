package com.tvd12.calabash.persist.action;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PersistDeleteOneAction implements PersistAction {

    protected final Object key;

    @Override
    public PersistActionType getType() {
        return PersistActionType.DELETE_ONE;
    }
}
