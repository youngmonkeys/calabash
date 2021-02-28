package com.tvd12.calabash.local.test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tvd12.calabash.Calabash;
import com.tvd12.calabash.core.EntityMap;
import com.tvd12.calabash.core.EntityMapPersist;
import com.tvd12.calabash.core.IAtomicLong;
import com.tvd12.calabash.core.annotation.MapPersistence;
import com.tvd12.calabash.core.statistic.StatisticsAware;
import com.tvd12.calabash.core.util.MapPersistenceAnnotations;
import com.tvd12.calabash.local.CalabashBuilder;
import com.tvd12.calabash.local.setting.SimpleEntityMapPersistSetting;
import com.tvd12.calabash.local.setting.SimpleEntityMapSetting;
import com.tvd12.calabash.local.setting.SimpleSettings;
import com.tvd12.calabash.local.test.mappersist.Person;
import com.tvd12.calabash.persist.factory.DefaultEntityMapPersistFactory;
import com.tvd12.ezyfox.bean.EzyBeanContext;

public class LocalMapPersistExample extends LocalBaseTest {

	@SuppressWarnings("rawtypes")
	public void test() throws Exception {
		SimpleSettings settings = new SimpleSettings();
		SimpleEntityMapPersistSetting mapPersistSetting = new SimpleEntityMapPersistSetting();
		mapPersistSetting.setWriteDelay(0);
		SimpleEntityMapSetting mapSetting = new SimpleEntityMapSetting();
		mapSetting.setMapName(CollectionNames.PERSON);
		mapSetting.setPersistSetting(mapPersistSetting);
		settings.addMapSetting(mapSetting);
		EzyBeanContext beanContext = newBeanContext();
		DefaultEntityMapPersistFactory mapPersistFactory = new DefaultEntityMapPersistFactory();
		List mapPersistences = beanContext.getSingletons(MapPersistence.class);
		for(Object mapPersist : mapPersistences) {
			String mapName = MapPersistenceAnnotations.getMapName(mapPersist);
			mapPersistFactory.addMapPersist(mapName, (EntityMapPersist) mapPersist);
		}
		Calabash calabash = new CalabashBuilder()
				.settings(settings)
				.mapPersistFactory(mapPersistFactory)
				.build();
		Person person = new Person(11, "person 6", 18);
		EntityMap<Long, Person> entityMap = calabash.getEntityMap(CollectionNames.PERSON);
		entityMap.put(person.getId(), person);
		
		IAtomicLong atomicLong = calabash.getAtomicLong("hello");
		Long newValue = atomicLong.addAndGet(100L);
		System.out.println("atomic long new value: " + newValue);
		
		Map<String, Object> statistics = new HashMap<>();
		while(true) {
			Thread.sleep(1000);
			((StatisticsAware)calabash).addStatistics(statistics);
			System.out.println("statistics: " + statistics);
		}
		
	}
	
	public static void main(String[] args) throws Exception {
		new LocalMapPersistExample().test();
	}
	
}
