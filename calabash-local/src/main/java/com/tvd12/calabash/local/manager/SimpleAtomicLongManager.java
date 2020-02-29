package com.tvd12.calabash.local.manager;

import java.util.HashMap;
import java.util.Map;

import com.tvd12.calabash.core.EntityMap;
import com.tvd12.calabash.core.IAtomicLong;
import com.tvd12.calabash.local.impl.AtomicLongImpl;

public class SimpleAtomicLongManager implements AtomicLongManager {

	protected final EntityMap<String, Long> map;
	protected final Map<String, IAtomicLong> atomicLongs;
	
	public SimpleAtomicLongManager(EntityMap<String, Long> map) {
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
