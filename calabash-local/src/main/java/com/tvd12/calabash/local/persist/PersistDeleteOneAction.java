package com.tvd12.calabash.local.persist;

import com.tvd12.calabash.core.persist.PersistAction;
import com.tvd12.calabash.core.persist.PersistActionType;

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
