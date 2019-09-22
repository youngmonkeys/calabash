package com.tvd12.calabash.core.test;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

import java.util.List;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.testng.annotations.Test;

import com.tvd12.calabash.core.manager.MapEvictionManager;
import com.tvd12.calabash.core.manager.MapManager;
import com.tvd12.calabash.eviction.MapEvictable;

public class MapEvictionManagerTest {

	@SuppressWarnings({ "rawtypes" })
	@Test
	public void test1() throws Exception {
		MapManager mapManager = mock(MapManager.class);
		doAnswer(new Answer() {
			@SuppressWarnings("unchecked")
			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {
				List buffer = (List)invocation.getArguments()[0];
				MapEvictable map = mock(MapEvictable.class);
				buffer.add(map);
				return buffer;
			}
		}).when(mapManager).getMapList(any(List.class));
		MapEvictionManager manager = MapEvictionManager.builder()
				.evictionInterval(1)
				.mapManager(mapManager)
				.build();
		manager.start();
		Thread.sleep(3000L);
		manager.stop();
	}
	
	@Test
	public void test2() throws Exception {
		MapManager mapManager = mock(MapManager.class);
		doThrow(RuntimeException.class).when(mapManager).getMapList(any(List.class));
		MapEvictionManager manager = MapEvictionManager.builder()
				.evictionInterval(1)
				.mapManager(mapManager)
				.build();
		manager.start();
		Thread.sleep(3000L);
		manager.stop();
	}
	
}
