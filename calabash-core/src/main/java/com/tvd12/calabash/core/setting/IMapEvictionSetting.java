package com.tvd12.calabash.core.setting;

public interface IMapEvictionSetting {

	int getKeyMaxIdleTime();
	
	default long getKeyMaxIdleTimeMilis() {
		long milis = getKeyMaxIdleTime() * 1000;
		return milis;
	}
	
}
