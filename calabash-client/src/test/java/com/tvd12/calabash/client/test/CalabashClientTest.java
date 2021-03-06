package com.tvd12.calabash.client.test;

import com.tvd12.calabash.client.CalabaseClient;
import com.tvd12.calabash.client.CalabashClientFactory;
import com.tvd12.calabash.core.EntityMap;
import com.tvd12.ezyfox.constant.EzyHasIntId;

public class CalabashClientTest {

	public void test() {
		CalabaseClient client = CalabashClientFactory.builder()
				.build()
				.newClient();
		EntityMap<String, String> map = client.getMap("world", String.class, String.class);
		System.out.println("mapId: " + ((EzyHasIntId)map).getId());
		map.set("Hello", "World");
		System.out.println("Hello " + map.get("Hello"));
	}
	
	public static void main(String[] args) {
		new CalabashClientTest().test();
	}
	
}
