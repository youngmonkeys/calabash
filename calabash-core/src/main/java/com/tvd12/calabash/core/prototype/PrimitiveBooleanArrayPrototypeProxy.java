package com.tvd12.calabash.core.prototype;

public final class PrimitiveBooleanArrayPrototypeProxy implements PrototypeProxy<boolean[]> {

    private static final PrimitiveBooleanArrayPrototypeProxy INSTANCE
        = new PrimitiveBooleanArrayPrototypeProxy();

    private PrimitiveBooleanArrayPrototypeProxy() {}

    public static PrimitiveBooleanArrayPrototypeProxy getInstance() {
        return INSTANCE;
    }

    @Override
    public boolean[] clone(Prototypes prototypes, boolean[] origin) {
        boolean[] copy = new boolean[origin.length];
        System.arraycopy(origin, 0, copy, 0, copy.length);
        return copy;
    }
}
