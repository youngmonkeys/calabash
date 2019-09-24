package com.tvd12.calabash.core.test;

import org.testng.annotations.Test;

import com.tvd12.calabash.Calabash;

public class CalabashTest {

	@Test(expectedExceptions = UnsupportedOperationException.class)
	public void test() {
		ExClalabash clalabash = new ExClalabash();
		clalabash.getEntityMap("");
	}
	
	@Test(expectedExceptions = UnsupportedOperationException.class)
	public void test2() {
		ExClalabash clalabash = new ExClalabash();
		clalabash.getBytesMap("");
	}
	
	public static class ExClalabash implements Calabash {
	}
	
}
