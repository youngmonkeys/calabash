package com.tvd12.calabash.core.test;

import com.tvd12.calabash.core.util.ByteArray;
import org.testng.annotations.Test;

import java.util.Arrays;

public class ByteArrayTest {

    @Test
    public void test() {
        ByteArray byteArray = ByteArray.wrap("abc".getBytes());
        assert byteArray.equals(new ByteArray("abc".getBytes()));
        //noinspection EqualsWithItself
        assert byteArray.equals(byteArray);
        //noinspection ConstantConditions
        assert !byteArray.equals(null);
        assert !byteArray.equals(new Object());
        assert !byteArray.equals(new ByteArray("".getBytes()));
        assert Arrays.equals(byteArray.getBytes(), "abc".getBytes());
        assert byteArray.hashCode() == Arrays.hashCode("abc".getBytes());
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void test2() {
        //noinspection ResultOfMethodCallIgnored
        ByteArray.wrap((byte[]) null);
    }
}
