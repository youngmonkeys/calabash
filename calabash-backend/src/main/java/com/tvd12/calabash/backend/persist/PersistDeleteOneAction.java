package com.tvd12.calabash.backend.persist;

import com.tvd12.calabash.core.persist.PersistAction;
import com.tvd12.calabash.core.persist.PersistActionType;
import com.tvd12.calabash.core.util.ByteArray;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PersistDeleteOneAction implements PersistAction {

	protected final ByteArray key;
	
	@Override
	public PersistActionType getType() {
		return PersistActionType.DELETE_ONE;
	}
	
}
