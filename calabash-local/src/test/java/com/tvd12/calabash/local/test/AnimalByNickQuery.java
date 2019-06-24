package com.tvd12.calabash.local.test;

import java.util.Map;

import com.tvd12.calabash.core.query.MapQuery;
import com.tvd12.ezyfox.util.EzyMapBuilder;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class AnimalByNickQuery implements MapQuery {

	@Getter
	protected final String nick;
	
	@SuppressWarnings("unchecked")
	@Override
	public Map<Object, Object> getKeys() {
		return EzyMapBuilder.mapBuilder()
				.put("nick", nick)
				.build();
	}
	
	@Override
	public int getQueryId() {
		return 0;
	}
	
}
