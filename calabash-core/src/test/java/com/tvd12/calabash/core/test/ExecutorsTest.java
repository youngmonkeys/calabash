package com.tvd12.calabash.core.test;

import com.tvd12.calabash.concurrent.Executors;
import org.testng.annotations.Test;

public class ExecutorsTest {

    @Test
    public void test() {
        Executors.newFixedThreadPool(1, "test");
    }
}
