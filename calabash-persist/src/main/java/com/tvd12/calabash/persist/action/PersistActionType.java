package com.tvd12.calabash.persist.action;

public enum PersistActionType {

	SAVE_ONE,
	SAVE_MANY,
	DELETE_ONE,
	DELETE_MANY;
	
	public boolean sames(PersistActionType other) {
		if(this == other)
			return true;
		switch (this) {
			case SAVE_ONE:
				return other == SAVE_MANY;
			case SAVE_MANY:
				return other == SAVE_ONE;
			case DELETE_ONE:
				return other == DELETE_MANY;
			default: 
				return other == DELETE_ONE;
		}
	}
}
