package com.tvd12.calabash.server.test;

import org.testng.annotations.Test;

import com.tvd12.calabash.server.CalabashServer;

public class CalabashServerTest {
	
	@Test
	public void test() throws Exception {
		CalabashServer server = new CalabashServer();
		server.start();
	}
	
	public static void main(String[] args) throws Exception {
		new CalabashServerTest().test();
	}
	
}
