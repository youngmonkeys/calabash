package com.tvd12.calabash.core.prototype;

import java.lang.reflect.Array;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@SuppressWarnings({"rawtypes", "unchecked"})
public class SimplePrototypes implements Prototypes {

    protected Map<Class, PrototypeProxy> proxies;

    public SimplePrototypes() {
        this(builder());
    }

    protected SimplePrototypes(Builder builder) {
        this.proxies = builder.proxies;
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public <T> T copy(T origin) {
        T copy = origin;
        if (origin == null) {
            return null;
        }
        if (origin instanceof Prototype) {
            copy = (T) ((Prototype) origin).clone();
        } else {
            Class objectType = origin.getClass();
            PrototypeProxy proxy = proxies.get(objectType);
            if (proxy != null) {
                copy = (T) proxy.clone(this, origin);
            } else if (origin instanceof Map) {
                copy = (T) copyMap((Map) origin);
            } else if (origin instanceof Set) {
                copy = (T) copySet((Set) origin);
            } else if (origin instanceof Collection) {
                copy = (T) copyList((List) origin);
            } else if (objectType.isArray()) {
                copy = (T) copyArray(origin);
            }
        }
        return copy;
    }

    @Override
    public <V> Set<V> copySet(Collection<V> origin) {
        Set<V> copy = new HashSet<>();
        for (V value : origin) {
            V copyValue = copy(value);
            copy.add(copyValue);
        }
        return copy;
    }

    @Override
    public <V> List<V> copyList(Collection<V> origin) {
        List<V> copy = new ArrayList<>();
        for (V value : origin) {
            V copyValue = copy(value);
            copy.add(copyValue);
        }
        return copy;
    }

    @Override
    public <K, V> Map<K, V> copyMap(Map<K, V> origin) {
        Map<K, V> copy = new HashMap<>();
        for (K key : origin.keySet()) {
            V value = origin.get(key);
            K copyKey = copy(key);
            V copyValue = copy(value);
            copy.put(copyKey, copyValue);
        }
        return copy;
    }

    protected Object copyArray(Object array) {
        Class itemType = array.getClass().getComponentType();
        int length = Array.getLength(array);
        Object copy = Array.newInstance(itemType, length);
        for (int i = 0; i < length; i++) {
            Object item = Array.get(array, i);
            Object copyItem = copy(item);
            Array.set(copy, i, copyItem);
        }
        return copy;
    }

    public static class Builder implements PrototypesBuilder {

        protected Map<Class, PrototypeProxy> proxies;

        public Builder() {
            this.proxies = defaultProxies();
        }

        @Override
        public PrototypesBuilder addProxy(Class objectType, PrototypeProxy proxy) {
            proxies.put(objectType, proxy);
            return this;
        }

        @Override
        public Prototypes build() {
            return new SimplePrototypes(this);
        }

        protected Map<Class, PrototypeProxy> defaultProxies() {
            Map<Class, PrototypeProxy> map = new ConcurrentHashMap<>();
            map.put(boolean[].class, PrimitiveBooleanArrayPrototypeProxy.getInstance());
            map.put(byte[].class, PrimitiveByteArrayPrototypeProxy.getInstance());
            map.put(char[].class, PrimitiveCharArrayPrototypeProxy.getInstance());
            map.put(double[].class, PrimitiveDoubleArrayPrototypeProxy.getInstance());
            map.put(float[].class, PrimitiveFloatArrayPrototypeProxy.getInstance());
            map.put(int[].class, PrimitiveIntArrayPrototypeProxy.getInstance());
            map.put(long[].class, PrimitiveLongArrayPrototypeProxy.getInstance());
            map.put(short[].class, PrimitiveShortArrayPrototypeProxy.getInstance());
            map.put(Boolean[].class, new ArrayPrototypeProxy<>(Boolean[]::new));
            map.put(Byte[].class, new ArrayPrototypeProxy<>(Byte[]::new));
            map.put(Character[].class, new ArrayPrototypeProxy<>(Character[]::new));
            map.put(Double[].class, new ArrayPrototypeProxy<>(Double[]::new));
            map.put(Float[].class, new ArrayPrototypeProxy<>(Float[]::new));
            map.put(Integer[].class, new ArrayPrototypeProxy<>(Integer[]::new));
            map.put(Long[].class, new ArrayPrototypeProxy<>(Long[]::new));
            map.put(Short[].class, new ArrayPrototypeProxy<>(Short[]::new));
            map.put(String[].class, new ArrayPrototypeProxy<>(String[]::new));
            return map;
        }
    }
}
