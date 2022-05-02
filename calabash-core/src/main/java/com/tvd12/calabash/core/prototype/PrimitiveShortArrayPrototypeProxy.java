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
        System.arraycopy(origin, 0, copy, 0, copy.length);
        return copy;
    }
}
