package com.tvd12.calabash;

import com.tvd12.calabash.core.IAtomicLong;

public interface Calabash extends CalabashBytes, CalabashEntity {

    String NAME = "calabash";

    IAtomicLong getAtomicLong(String name);
}
