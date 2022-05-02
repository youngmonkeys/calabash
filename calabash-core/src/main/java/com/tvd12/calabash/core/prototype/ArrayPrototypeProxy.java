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
        System.arraycopy(origin, 0, copy, 0, copy.length);
        return copy;
    }
}
