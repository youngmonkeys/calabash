package com.tvd12.calabash.core.prototype;

public final class PrimitiveDoubleArrayPrototypeProxy implements PrototypeProxy<double[]> {

	private static final PrimitiveDoubleArrayPrototypeProxy INSTANCE 
			= new PrimitiveDoubleArrayPrototypeProxy();
	
	private PrimitiveDoubleArrayPrototypeProxy() {}
	
	public static PrimitiveDoubleArrayPrototypeProxy getInstance() {
		return INSTANCE;
	}
	
	@Override
	public double[] clone(Prototypes prototypes, double[] origin) {
		double[] copy = new double[origin.length];
		for(int i = 0 ; i < copy.length ; ++i)
			copy[i] = origin[i];
		return copy;
	}
	
}
