package com.tvd12.calabash.backend.builder;

import com.tvd12.calabash.Calabash;
import com.tvd12.calabash.backend.impl.CalabashImpl;
import com.tvd12.calabash.backend.setting.Settings;
import com.tvd12.calabash.factory.DefaultEntityMapPersistFactory;
import com.tvd12.calabash.factory.EntityMapPersistFactory;
import com.tvd12.ezyfox.builder.EzyBuilder;
import com.tvd12.ezyfox.codec.EzyEntityCodec;

import lombok.Getter;

@Getter
public class CalabashBuilder implements EzyBuilder<Calabash> {

	protected Settings settings;
	protected EzyEntityCodec entityCodec;
	protected EntityMapPersistFactory mapPersistFactory;
	
	public CalabashBuilder settings(Settings settings) {
		this.settings = settings;
		return this;
	}
	
	public CalabashBuilder entityCodec(EzyEntityCodec entityCodec) {
		this.entityCodec = entityCodec;
		return this;
	}
	
	public CalabashBuilder mapPersistFactory(EntityMapPersistFactory mapPersistFactory) {
		this.mapPersistFactory = mapPersistFactory;
		return this;
	}
	
	@Override
	public Calabash build() {
		if(mapPersistFactory == null)
			mapPersistFactory = new DefaultEntityMapPersistFactory();
		return new CalabashImpl(this);
	}
	
}
