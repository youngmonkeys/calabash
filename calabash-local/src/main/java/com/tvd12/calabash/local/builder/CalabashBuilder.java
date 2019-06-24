package com.tvd12.calabash.local.builder;

import com.tvd12.calabash.Calabash;
import com.tvd12.calabash.local.factory.EntityUniqueFactory;
import com.tvd12.calabash.local.factory.SimpleEntityUniqueFactory;
import com.tvd12.calabash.local.impl.CalabashImpl;
import com.tvd12.calabash.local.setting.Settings;
import com.tvd12.calabash.persist.factory.DefaultEntityMapPersistFactory;
import com.tvd12.calabash.persist.factory.EntityMapPersistFactory;
import com.tvd12.ezyfox.builder.EzyBuilder;

import lombok.Getter;

@Getter
public class CalabashBuilder implements EzyBuilder<Calabash> {

	protected Settings settings;
	protected EntityUniqueFactory uniqueFactory;
	protected EntityMapPersistFactory mapPersistFactory;
	
	public CalabashBuilder settings(Settings settings) {
		this.settings = settings;
		return this;
	}
	
	public CalabashBuilder uniqueFactory(EntityUniqueFactory uniqueFactory) {
		this.uniqueFactory = uniqueFactory;
		return this;
	}
	
	public CalabashBuilder mapPersistFactory(EntityMapPersistFactory mapPersistFactory) {
		this.mapPersistFactory = mapPersistFactory;
		return this;
	}
	
	@Override
	public Calabash build() {
		if(uniqueFactory == null)
			uniqueFactory = new SimpleEntityUniqueFactory();
		if(mapPersistFactory == null)
			mapPersistFactory = new DefaultEntityMapPersistFactory();
		return new CalabashImpl(this);
	}
	
}
