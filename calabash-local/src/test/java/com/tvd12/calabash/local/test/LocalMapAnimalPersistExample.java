package com.tvd12.calabash.local.test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tvd12.calabash.Calabash;
import com.tvd12.calabash.core.EntityMap;
import com.tvd12.calabash.core.statistic.StatisticsAware;
import com.tvd12.calabash.local.CalabashBuilder;
import com.tvd12.calabash.local.setting.SimpleEntityMapPersistSetting;
import com.tvd12.calabash.local.setting.SimpleEntityMapSetting;
import com.tvd12.calabash.local.setting.SimpleSettings;
import com.tvd12.calabash.local.test.mappersist.Animal;
import com.tvd12.calabash.persist.EntityMapPersist;
import com.tvd12.calabash.persist.annotation.MapPersistence;
import com.tvd12.calabash.persist.factory.SimpleEntityMapPersistFactory;
import com.tvd12.calabash.persist.util.MapPersistenceAnnotations;
import com.tvd12.ezyfox.bean.EzyBeanContext;

public class LocalMapAnimalPersistExample extends LocalBaseTest {

	@SuppressWarnings("rawtypes")
	public void test()throws Exception {
		SimpleSettings settings = new SimpleSettings();
		SimpleEntityMapPersistSetting mapPersistSetting = new SimpleEntityMapPersistSetting();
		mapPersistSetting.setWriteDelay(0);
		SimpleEntityMapSetting mapSetting = new SimpleEntityMapSetting();
		mapSetting.setMapName(CollectionNames.ANIMAL);
		mapSetting.setPersistSetting(mapPersistSetting);
		settings.addMapSetting(mapSetting);
		EzyBeanContext beanContext = newBeanContext();
		SimpleEntityMapPersistFactory.Builder mapPersistFactoryBuilder = 
				SimpleEntityMapPersistFactory.builder();
		List mapPersistences = beanContext.getSingletons(MapPersistence.class);
		for(Object mapPersist : mapPersistences) {
			String mapName = MapPersistenceAnnotations.getMapName(mapPersist);
			mapPersistFactoryBuilder.addMapPersist(mapName, (EntityMapPersist) mapPersist);
		}
		
		Calabash calabash = new CalabashBuilder()
				.settings(settings)
				.mapPersistFactory(mapPersistFactoryBuilder.build())
				.build();
		Animal animal = new Animal(2, "animal 2", "cat");
		EntityMap<Long, Animal> entityMap = calabash.getEntityMap(CollectionNames.ANIMAL);
		entityMap.put(animal.getId(), animal);
		AnimalByNickQuery query = new AnimalByNickQuery(animal.getNick());
		Animal animalByQuery = entityMap.getByQuery(1L, query);
		System.out.println("animal by query: " + animalByQuery);
		
		Map<String, Object> statistics = new HashMap<>();
		while(true) {
			Thread.sleep(1000);
			((StatisticsAware)calabash).addStatistics(statistics);
			System.out.println("statistics: " + statistics);
		}
		
	}
	
	public static void main(String[] args) throws Exception {
		new LocalMapAnimalPersistExample().test();
	}
	
}
