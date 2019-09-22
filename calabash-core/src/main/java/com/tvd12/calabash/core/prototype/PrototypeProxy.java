package com.tvd12.calabash.core.prototype;

public interface PrototypeProxy<T> {

	T clone(Prototypes prototypes, T origin);
	
}
