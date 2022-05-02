package com.tvd12.calabash.local.impl;

import com.tvd12.calabash.core.EntityMap;
import com.tvd12.calabash.core.IAtomicLong;
import lombok.Getter;

public class AtomicLongImpl implements IAtomicLong {

    @Getter
    protected final String name;
    protected final EntityMap<String, Long> map;

    public AtomicLongImpl(String name, EntityMap<String, Long> map) {
        this.map = map;
        this.name = name;
    }

    @Override
    public long get() {
        synchronized (this) {
            Long current = map.get(name);
            return current == null ? 0L : current;
        }
    }

    @Override
    public long addAndGet(long delta) {
        synchronized (this) {
            Long current = map.get(name);
            long newValue = current == null ? delta : current + delta;
            map.put(name, newValue);
            return newValue;
        }
    }
}
