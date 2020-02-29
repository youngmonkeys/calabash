package com.tvd12.calabash.server.core.manager;

import java.util.HashMap;
import java.util.Map;

import com.tvd12.calabash.core.BytesMap;
import com.tvd12.calabash.core.IAtomicLong;
import com.tvd12.calabash.server.core.impl.AtomicLongImpl;

public class SimpleAtomicLongManager implements AtomicLongManager {

	protected final BytesMap map;
	protected final Map<String, IAtomicLong> atomicLongs;
	
	public SimpleAtomicLongManager(BytesMap map) {
		this.map = map;
		this.atomicLongs = new HashMap<>();
	}
	
	@Override
	public IAtomicLong getAtomicLong(String name) {
		IAtomicLong atomicLong = atomicLongs.get(name);
		if(atomicLong == null)
			atomicLong = newAtomicLong(name);
		return atomicLong;
	}
	
	protected IAtomicLong newAtomicLong(String name) {
		synchronized (atomicLongs) {
			IAtomicLong atomicLong = atomicLongs.get(name);
			if(atomicLong == null) {
				atomicLong = new AtomicLongImpl(name, map);
				atomicLongs.put(name, atomicLong);
			}
			return atomicLong;
		}
	}

}
