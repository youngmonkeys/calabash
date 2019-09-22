package com.tvd12.calabash.core.test;

import org.testng.annotations.Test;

import com.tvd12.calabash.concurrent.Executors;

public class ExecutorsTest {

	@Test
	public void test() {
		Executors.newFixedThreadPool(1, "test");
	}
	
}
