package com.tvd12.calabash.local.persist;

import java.util.Set;

import com.tvd12.calabash.core.util.ByteArray;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PersistDeleteManyAction implements PersistAction {

	protected final Set<ByteArray> keys;
	
	@Override
	public PersistActionType getType() {
		return PersistActionType.DELTE_MANY;
	}
	
}
