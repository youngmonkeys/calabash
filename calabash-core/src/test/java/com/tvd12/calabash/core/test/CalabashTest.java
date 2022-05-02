package com.tvd12.calabash.core.test;

import com.tvd12.calabash.Calabash;
import com.tvd12.calabash.core.IAtomicLong;
import org.testng.annotations.Test;

public class CalabashTest {

    @Test(expectedExceptions = UnsupportedOperationException.class)
    public void test() {
        ExCalabash calabash = new ExCalabash();
        calabash.getEntityMap("");
    }

    @Test(expectedExceptions = UnsupportedOperationException.class)
    public void test2() {
        ExCalabash calabash = new ExCalabash();
        calabash.getBytesMap("");
    }

    public static class ExCalabash implements Calabash {

        @Override
        public IAtomicLong getAtomicLong(String name) {
            return null;
        }
    }
}
