package com.tvd12.calabash.server.core.builder;

import com.tvd12.calabash.Calabash;
import com.tvd12.calabash.persist.factory.DefaultEntityMapPersistFactory;
import com.tvd12.calabash.persist.factory.EntityMapPersistFactory;
import com.tvd12.calabash.server.core.impl.CalabashImpl;
import com.tvd12.calabash.server.core.setting.Settings;
import com.tvd12.ezyfox.builder.EzyBuilder;
import com.tvd12.ezyfox.codec.EzyEntityCodec;

import lombok.Getter;

@Getter
public class CalabashBuilder implements EzyBuilder<Calabash> {

	protected Settings settings;
	protected EzyEntityCodec entityCodec;
	protected EntityMapPersistFactory entityMapPersistFactory;
	
	public CalabashBuilder settings(Settings settings) {
		this.settings = settings;
		return this;
	}
	
	public CalabashBuilder entityCodec(EzyEntityCodec entityCodec) {
		this.entityCodec = entityCodec;
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
