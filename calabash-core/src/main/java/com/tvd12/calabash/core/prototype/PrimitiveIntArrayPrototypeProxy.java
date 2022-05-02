package com.tvd12.calabash.core.prototype;

public final class PrimitiveIntArrayPrototypeProxy implements PrototypeProxy<int[]> {

    private static final PrimitiveIntArrayPrototypeProxy INSTANCE
        = new PrimitiveIntArrayPrototypeProxy();

    private PrimitiveIntArrayPrototypeProxy() {}

    public static PrimitiveIntArrayPrototypeProxy getInstance() {
        return INSTANCE;
    }

    @Override
    public int[] clone(Prototypes prototypes, int[] origin) {
        int[] copy = new int[origin.length];
        System.arraycopy(origin, 0, copy, 0, copy.length);
        return copy;
    }
}
