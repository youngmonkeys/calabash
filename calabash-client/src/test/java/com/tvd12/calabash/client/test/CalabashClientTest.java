package com.tvd12.calabash.client.test;

import com.tvd12.calabash.client.CalabaseClient;
import com.tvd12.calabash.client.CalabashClientFactory;

public class CalabashClientTest {

	public void test() {
		CalabaseClient client = CalabashClientFactory.builder()
				.build()
				.newClient();
		client.getMap("hello", String.class, String.class);
	}
	
	public static void main(String[] args) {
		new CalabashClientTest().test();
	}
	
}
