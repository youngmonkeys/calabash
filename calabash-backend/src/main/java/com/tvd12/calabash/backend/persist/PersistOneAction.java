package com.tvd12.calabash.backend.persist;

import com.tvd12.calabash.core.util.ByteArray;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PersistOneAction implements PersistAction {

	protected final ByteArray key;
	protected final byte[] value;
	
	@Override
	public PersistActionType getType() {
		return PersistActionType.PERSIST_ONE;
	}
	
}
