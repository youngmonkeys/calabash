package com.tvd12.calabash.core;

public interface IAtomicLong {

	long addAndGet(long delta);
	
	String getName();
	
	default long incrementAndGet() {
		return addAndGet(1);
	}
	
}
