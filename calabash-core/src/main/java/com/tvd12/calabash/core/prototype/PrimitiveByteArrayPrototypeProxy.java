package com.tvd12.calabash.core.prototype;

public final class PrimitiveByteArrayPrototypeProxy implements PrototypeProxy<byte[]> {

	private static final PrimitiveByteArrayPrototypeProxy INSTANCE 
			= new PrimitiveByteArrayPrototypeProxy();
	
	private PrimitiveByteArrayPrototypeProxy() {}
	
	public static PrimitiveByteArrayPrototypeProxy getInstance() {
		return INSTANCE;
	}
	
	@Override
	public byte[] clone(Prototypes prototypes, byte[] origin) {
		byte[] copy = new byte[origin.length];
		for(int i = 0 ; i < copy.length ; ++i)
			copy[i] = origin[i];
		return copy;
	}
	
}
