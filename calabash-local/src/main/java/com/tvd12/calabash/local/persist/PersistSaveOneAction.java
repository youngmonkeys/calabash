package com.tvd12.calabash.local.persist;

import com.tvd12.calabash.core.persist.PersistAction;
import com.tvd12.calabash.core.persist.PersistActionType;

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
