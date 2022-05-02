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
        System.arraycopy(origin, 0, copy, 0, copy.length);
        return copy;
    }
}
