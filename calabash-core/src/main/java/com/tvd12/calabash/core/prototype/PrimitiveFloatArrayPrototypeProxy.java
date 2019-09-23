package com.tvd12.calabash.core.prototype;

public final class PrimitiveFloatArrayPrototypeProxy implements PrototypeProxy<float[]> {

	private static final PrimitiveFloatArrayPrototypeProxy INSTANCE 
			= new PrimitiveFloatArrayPrototypeProxy();
	
	private PrimitiveFloatArrayPrototypeProxy() {}
	
	public static PrimitiveFloatArrayPrototypeProxy getInstance() {
		return INSTANCE;
	}
	
	@Override
	public float[] clone(Prototypes prototypes, float[] origin) {
		float[] copy = new float[origin.length];
		for(int i = 0 ; i < copy.length ; ++i)
			copy[i] = origin[i];
		return copy;
	}
	
}
