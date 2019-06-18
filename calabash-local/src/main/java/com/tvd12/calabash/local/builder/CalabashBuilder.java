package com.tvd12.calabash.local.builder;

import com.tvd12.calabash.Calabash;
import com.tvd12.calabash.factory.DefaultEntityMapPersistFactory;
import com.tvd12.calabash.factory.EntityMapPersistFactory;
import com.tvd12.calabash.local.impl.CalabashImpl;
import com.tvd12.calabash.local.setting.Settings;
import com.tvd12.ezyfox.builder.EzyBuilder;

import lombok.Getter;

@Getter
public class CalabashBuilder implements EzyBuilder<Calabash> {

	protected Settings settings;
	protected EntityMapPersistFactory entityMapPersistFactory;
	
	public CalabashBuilder settings(Settings settings) {
		this.settings = settings;
		return this;
	}
	
	public CalabashBuilder entityMapPersistFactory(EntityMapPersistFactory entityMapPersistFactory) {
		this.entityMapPersistFactory = entityMapPersistFactory;
		return this;
	}
	
	@Override
	public Calabash build() {
		if(entityMapPersistFactory == null)
			entityMapPersistFactory = new DefaultEntityMapPersistFactory();
		return new CalabashImpl(this);
	}
	
}
