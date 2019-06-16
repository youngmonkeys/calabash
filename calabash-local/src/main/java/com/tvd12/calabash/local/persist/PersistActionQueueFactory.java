package com.tvd12.calabash.local.persist;

import com.tvd12.calabash.local.setting.EntitySettings;

public class PersistActionQueueFactory {
	
	protected final EntitySettings settings;
	
	public PersistActionQueueFactory(EntitySettings settings) {
		this.settings = settings;
	}
	
	public PersistActionQueue newActionQueue(String mapName) {
		return new PersistActionQueue();
	}
	
}
