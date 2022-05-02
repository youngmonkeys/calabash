package com.tvd12.calabash.persist.test.util;

import com.tvd12.calabash.persist.annotation.MapPersistence;
import com.tvd12.calabash.persist.util.MapPersistenceAnnotations;
import org.testng.annotations.Test;

public class MapPersistenceAnnotationsTest {

    @Test
    public void test() {
        assert MapPersistenceAnnotations.getMapName(new A()).equals("a");
        assert MapPersistenceAnnotations.getMapName(new BZ()).equals("b");
    }

    @MapPersistence
    public static class A {
    }

    @MapPersistence("b")
    public static class BZ {
    }
}
