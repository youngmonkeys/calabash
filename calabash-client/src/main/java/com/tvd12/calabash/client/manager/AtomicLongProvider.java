package com.tvd12.calabash.client.manager;

import java.util.HashMap;
import java.util.Map;

import com.tvd12.calabash.client.factory.AtomicLongFactory;
import com.tvd12.calabash.core.IAtomicLong;
import com.tvd12.ezyfox.builder.EzyBuilder;

public class AtomicLongProvider {

	protected final AtomicLongFactory atomicLongFactory;
	protected final Map<String, IAtomicLong> atomicLongs;
	
	protected AtomicLongProvider(Builder builder) {
		this.atomicLongs = new HashMap<>();
		this.atomicLongFactory = builder.atomicLongFactory;
	}
	
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
				atomicLong = atomicLongFactory.newAtomicLong(name);
				atomicLongs.put(name, atomicLong);
			}
			return atomicLong;
		}
	}
	
	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder implements EzyBuilder<AtomicLongProvider> {
		
		protected AtomicLongFactory atomicLongFactory;
		
		public Builder atomicLongFactory(AtomicLongFactory atomicLongFactory) {
			this.atomicLongFactory = atomicLongFactory;
			return this;
		}
		
		@Override
		public AtomicLongProvider build() {
			return new AtomicLongProvider(this);
		}
		
	}
	
}
