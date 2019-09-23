package com.tvd12.calabash.core.prototype;

public final class PrimitiveCharArrayPrototypeProxy implements PrototypeProxy<char[]> {

	private static final PrimitiveCharArrayPrototypeProxy INSTANCE 
			= new PrimitiveCharArrayPrototypeProxy();
	
	private PrimitiveCharArrayPrototypeProxy() {}
	
	public static PrimitiveCharArrayPrototypeProxy getInstance() {
		return INSTANCE;
	}
	
	@Override
	public char[] clone(Prototypes prototypes, char[] origin) {
		char[] copy = new char[origin.length];
		for(int i = 0 ; i < copy.length ; ++i)
			copy[i] = origin[i];
		return copy;
	}
	
}
