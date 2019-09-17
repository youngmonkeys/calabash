package com.tvd12.calabash.core.setting;

public interface IMapSetting {

	String getMapName();
	
	int getMaxPartition();

	IMapPersistSetting getPersistSetting();
	
	IMapEvictionSetting getEvictionSetting();
	
}
