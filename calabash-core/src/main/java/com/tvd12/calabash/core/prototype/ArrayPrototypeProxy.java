package com.tvd12.calabash.core.prototype;

import java.util.function.Function;

public class ArrayPrototypeProxy<T> implements PrototypeProxy<T[]> {

	protected final Function<Integer, T[]> generator;
	
	public ArrayPrototypeProxy(Function<Integer, T[]> generator) {
		this.generator = generator;
	}
	
	@Override
	public T[] clone(Prototypes prototypes, T[] origin) {
		T[] copy = generator.apply(origin.length);
		for(int i = 0 ; i < copy.length ; ++i)
			copy[i] = origin[i];
		return copy;
	}
	
}
