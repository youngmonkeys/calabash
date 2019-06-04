package com.tvd12.calabash.backend.persist;

import java.util.Map;

import com.tvd12.calabash.core.util.ByteArray;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PersistManyAction implements PersistAction {

	protected final Map<ByteArray, byte[]> keyValues;
	
	@Override
	public PersistActionType getType() {
		return PersistActionType.PERSIST_MANY;
	}
	
}
