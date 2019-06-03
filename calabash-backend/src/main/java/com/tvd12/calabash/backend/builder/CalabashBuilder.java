package com.tvd12.calabash.backend.builder;

import com.tvd12.calabash.Calabash;
import com.tvd12.calabash.backend.factory.DefaultMapPersistFactory;
import com.tvd12.calabash.backend.factory.MapPersistFactory;
import com.tvd12.calabash.backend.impl.CalabashImpl;
import com.tvd12.calabash.backend.setting.Settings;
import com.tvd12.ezyfox.builder.EzyBuilder;
import com.tvd12.ezyfox.codec.EzyEntityCodec;

import lombok.Getter;

@Getter
public class CalabashBuilder implements EzyBuilder<Calabash> {

	protected Settings settings;
	protected EzyEntityCodec entityCodec;
	protected MapPersistFactory mapPersistFactory;
	
	public CalabashBuilder settings(Settings settings) {
		this.settings = settings;
		return this;
	}
	
	public CalabashBuilder entityCodec(EzyEntityCodec entityCodec) {
		this.entityCodec = entityCodec;
		return this;
	}
	
	public CalabashBuilder mapPersistFactory(MapPersistFactory mapPersistFactory) {
		this.mapPersistFactory = mapPersistFactory;
		return this;
	}
	
	@Override
	public Calabash build() {
		if(mapPersistFactory == null)
			mapPersistFactory = new DefaultMapPersistFactory();
		return new CalabashImpl(this);
	}
	
}
