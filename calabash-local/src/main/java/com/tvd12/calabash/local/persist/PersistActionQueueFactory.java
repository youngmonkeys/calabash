package com.tvd12.calabash.local.persist;

import com.tvd12.calabash.core.persist.PersistActionQueue;
import com.tvd12.calabash.local.setting.Settings;

public class PersistActionQueueFactory {
	
	protected final Settings settings;
	
	public PersistActionQueueFactory(Settings settings) {
		this.settings = settings;
	}
	
	public PersistActionQueue newActionQueue(String mapName) {
		return new PersistActionQueue();
	}
	
}
