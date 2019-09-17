package com.tvd12.calabash.core.manager;

import java.util.concurrent.locks.Lock;

public interface LockManager {

	Lock getLock(Object key);
	
	
	
}
