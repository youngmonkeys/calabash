package com.tvd12.calabash.backend.provider;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;

import com.tvd12.calabash.core.util.ByteArray;
import com.tvd12.ezyfox.function.EzyFunctions;

public class MapLockProvider {

	protected final ConcurrentHashMap<ByteArray, Lock> locks;
	
	public MapLockProvider() {
		this.locks = new ConcurrentHashMap<>();
	}
	
	public Lock provideLock(ByteArray key) {
		Lock lock = locks.computeIfAbsent(key, EzyFunctions.NEW_REENTRANT_LOCK_FUNC);
		return lock;
	}
	
	public void removeLock(ByteArray key) {
		this.locks.remove(key);
	}
	
}
