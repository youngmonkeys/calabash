package com.tvd12.calabash.server.test;

import com.tvd12.calabash.server.CalabashServer;
import org.testng.annotations.Test;

public class CalabashServerTest {

    public static void main(String[] args) throws Exception {
        new CalabashServerTest().test();
    }

    @Test
    public void test() throws Exception {
        CalabashServer server = new CalabashServer();
        server.start();
    }

}
