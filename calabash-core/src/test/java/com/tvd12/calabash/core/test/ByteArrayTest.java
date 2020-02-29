package com.tvd12.calabash.core.test;

import java.util.Arrays;

import org.testng.annotations.Test;

import com.tvd12.calabash.core.util.ByteArray;

public class ByteArrayTest {

	@Test
	public void test() {
		ByteArray byteArray = ByteArray.wrap("abc".getBytes());
		assert byteArray.equals(new ByteArray("abc".getBytes()));
		assert byteArray.equals(byteArray);
		assert !byteArray.equals(null);
		assert !byteArray.equals(new Object());
		assert !byteArray.equals(new ByteArray("".getBytes()));
		assert Arrays.equals(byteArray.getBytes(), "abc".getBytes());
		assert byteArray.hashCode() == Arrays.hashCode("abc".getBytes());
	}
	
	@Test(expectedExceptions = NullPointerException.class)
	public void test2() {
		ByteArray.wrap((byte[])null);
	}
	
}
