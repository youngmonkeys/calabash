package com.tvd12.calabash.persist.test;

import com.tvd12.calabash.persist.EntityMapPersist;
import com.tvd12.ezyfox.collect.Sets;
import com.tvd12.ezyfox.exception.UnimplementedOperationException;
import com.tvd12.ezyfox.util.EzyMapBuilder;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

public class EntityMapPersistTest {

    @SuppressWarnings("unchecked")
    @Test
    public void test() {
        ExEntityMapPersist mapPersist = new ExEntityMapPersist();
        mapPersist.persist(EzyMapBuilder.mapBuilder()
            .put(1, "a")
            .put(2, "b")
            .put(3, "C")
            .build());
        Map<Integer, String> values = mapPersist.load(Sets.newHashSet(1, 2));
        assert values.size() == 2;
        assert mapPersist.loadByQuery(null) == null;
        assert mapPersist.getKeyType() == Integer.class;
        assert mapPersist.getValueType() == String.class;
        mapPersist.delete(1);
        mapPersist.delete(Sets.newHashSet(1, 2, 3));
        mapPersist.loadAll();
    }

    @Test(expectedExceptions = UnimplementedOperationException.class)
    public void test2() {
        UnknownMapPersist p = new UnknownMapPersist();
        p.getKeyType();
    }

    @Test(expectedExceptions = UnimplementedOperationException.class)
    public void test3() {
        UnknownMapPersist p = new UnknownMapPersist();
        p.getValueType();
    }

    @SuppressWarnings("rawtypes")
    public static class UnknownMapPersist implements EntityMapPersist {

        @Override
        public Object load(Object key) {
            return null;
        }

        @Override
        public void persist(Object key, Object value) {

        }
    }

    public static class ExEntityMapPersist implements EntityMapPersist<Integer, String> {

        protected final Map<Integer, String> store = new HashMap<>();

        @Override
        public String load(Integer key) {
            return store.get(key);
        }

        @Override
        public void persist(Integer key, String value) {
            store.put(key, value);
        }
    }
}
