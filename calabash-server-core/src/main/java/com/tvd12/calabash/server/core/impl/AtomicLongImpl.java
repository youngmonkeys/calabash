package com.tvd12.calabash.server.core.impl;

import com.tvd12.calabash.core.BytesMap;
import com.tvd12.calabash.core.IAtomicLong;
import com.tvd12.calabash.core.util.ByteArray;

import lombok.Getter;

public class AtomicLongImpl implements IAtomicLong {

	@Getter
	protected final String name;
	protected final BytesMap map;
	protected final ByteArray nameBytes;
	
	public AtomicLongImpl(String name, BytesMap map) {
		this.map = map;
		this.name = name;
		this.nameBytes = ByteArray.wrap(name);
	}
	
	@Override
	public long get() {
		return addAndGet(0);
	}
	
	@Override
	public long addAndGet(long delta) {
		return map.addAndGet(nameBytes, delta);
	}
	
	@Override
	public String toString() {
		return "AtomicLong(" + name + ")";
	}
	
}
