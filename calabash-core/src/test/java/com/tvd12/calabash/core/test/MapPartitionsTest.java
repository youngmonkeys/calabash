package com.tvd12.calabash.core.test;

import static org.testng.Assert.assertEquals;

import java.util.Map;
import java.util.Set;

import org.testng.annotations.Test;

import com.tvd12.calabash.core.util.MapPartitions;
import com.tvd12.ezyfox.collect.Lists;
import com.tvd12.ezyfox.collect.Sets;
import com.tvd12.ezyfox.util.EzyMapBuilder;

public class MapPartitionsTest {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	public void test() {
		int partitionIndex = MapPartitions.getPartitionIndex(2, new A(-3));
		assertEquals(partitionIndex, 1);
		Map<Integer, Set<A>> classifyKeys = MapPartitions.classifyKeys(3, Lists.newArrayList(new A(0), new A(1), new A(2), new A(3), new A(5)));
		assert classifyKeys.size() == 3;
		System.out.println("classifyKeys: " + classifyKeys);
		assertEquals((Object)classifyKeys.get(0), Sets.newHashSet(new A(0), new A(3)));
		assertEquals((Object)classifyKeys.get(1), Sets.newHashSet(new A(1)));
		assertEquals((Object)classifyKeys.get(2), Sets.newHashSet(new A(2), new A(5)));
		Map map = EzyMapBuilder.mapBuilder()
				.put(new A(0), 0)
				.put(new A(1), 1)
				.put(new A(2), 2)
				.put(new A(3), 3)
				.put(new A(5), 5)
				.build();
		Map<Integer, Map> classifyMaps = MapPartitions.classifyMaps(3, map);
		assert classifyMaps.size() == 3;
		System.out.println("classifyMaps: " + classifyMaps);
		assertEquals(classifyMaps.get(0).size(), 2);
		assertEquals(classifyMaps.get(1).size(), 1);
		assertEquals(classifyMaps.get(2).size(), 2);
	}
	
	public static class A {
		
		private int id;
		
		public A(int id) {
			this.id = id;
		}
		
		@Override
		public int hashCode() {
			return id;
		}
		
		@Override
		public boolean equals(Object obj) {
			return ((A)obj).id == id;
		}
		
		@Override
		public String toString() {
			return String.valueOf(id);
		}
	}
	
}
