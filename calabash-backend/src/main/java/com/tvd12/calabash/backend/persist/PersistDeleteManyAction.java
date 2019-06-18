package com.tvd12.calabash.backend.persist;

import java.util.Set;

import com.tvd12.calabash.core.persist.PersistAction;
import com.tvd12.calabash.core.persist.PersistActionType;
import com.tvd12.calabash.core.util.ByteArray;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PersistDeleteManyAction implements PersistAction {

	protected final Set<ByteArray> keys;
	
	@Override
	public PersistActionType getType() {
		return PersistActionType.DELETE_MANY;
	}
	
}
