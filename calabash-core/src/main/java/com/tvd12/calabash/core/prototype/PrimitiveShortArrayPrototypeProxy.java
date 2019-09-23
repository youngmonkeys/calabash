package com.tvd12.calabash.core.prototype;

public final class PrimitiveShortArrayPrototypeProxy implements PrototypeProxy<short[]> {

	private static final PrimitiveShortArrayPrototypeProxy INSTANCE 
			= new PrimitiveShortArrayPrototypeProxy();
	
	private PrimitiveShortArrayPrototypeProxy() {}
	
	public static PrimitiveShortArrayPrototypeProxy getInstance() {
		return INSTANCE;
	}
	
	@Override
	public short[] clone(Prototypes prototypes, short[] origin) {
		short[] copy = new short[origin.length];
		for(int i = 0 ; i < copy.length ; ++i)
			copy[i] = origin[i];
		return copy;
	}
	
}
