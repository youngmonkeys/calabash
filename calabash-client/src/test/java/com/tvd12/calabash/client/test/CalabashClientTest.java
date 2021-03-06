package com.tvd12.calabash.client.test;

import com.tvd12.calabash.client.CalabaseClient;
import com.tvd12.calabash.client.CalabashClientFactory;
import com.tvd12.calabash.core.EntityMap;
import com.tvd12.ezyfox.collect.Sets;
import com.tvd12.ezyfox.constant.EzyHasIntId;
import com.tvd12.ezyfox.util.EzyMapBuilder;

public class CalabashClientTest {

	@SuppressWarnings("unchecked")
	public void test() {
		CalabaseClient client = CalabashClientFactory.builder()
				.build()
				.newClient();
		EntityMap<String, String> map = client.getMap("world", String.class, String.class);
		System.out.println("mapId: " + ((EzyHasIntId)map).getId());
		map.set("Hello", "World");
		System.out.println("set ok");
		map.put("Foo", "Bar");
		System.out.println("put ok");
		map.putAll(EzyMapBuilder.mapBuilder()
				.put("Who", "Are You?")
				.build());
		System.out.println("Hello " + map.get("Hello"));
		System.out.println("Hello " + map.get(Sets.newHashSet(
				"Hello", "Who")));
	}
	
	public static void main(String[] args) {
		new CalabashClientTest().test();
	}
	
}
