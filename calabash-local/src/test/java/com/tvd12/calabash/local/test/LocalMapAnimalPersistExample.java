package com.tvd12.calabash.local.test;

import static com.tvd12.ezyfox.util.EzyAutoImplAnnotations.getBeanName;

import java.util.List;
import java.util.Map;

import com.mongodb.MongoClient;
import com.tvd12.calabash.Calabash;
import com.tvd12.calabash.core.EntityMap;
import com.tvd12.calabash.core.EntityMapPersist;
import com.tvd12.calabash.core.annotation.MapPersistence;
import com.tvd12.calabash.core.util.MapPersistenceAnnotations;
import com.tvd12.calabash.local.builder.CalabashBuilder;
import com.tvd12.calabash.local.factory.EntityUniqueFactory;
import com.tvd12.calabash.local.factory.SimpleEntityUniqueFactory;
import com.tvd12.calabash.local.factory.SimpleEntityUniqueFactory.UniqueKeyMapsBuilder;
import com.tvd12.calabash.local.setting.SimpleEntityMapPersistSetting;
import com.tvd12.calabash.local.setting.SimpleEntityMapSetting;
import com.tvd12.calabash.local.setting.SimpleSettings;
import com.tvd12.calabash.local.test.mappersist.Animal;
import com.tvd12.calabash.persist.factory.DefaultEntityMapPersistFactory;
import com.tvd12.ezyfox.bean.EzyBeanContext;
import com.tvd12.ezyfox.bean.EzyBeanContextBuilder;
import com.tvd12.ezyfox.mongodb.loader.EzyInputStreamMongoClientLoader;
import com.tvd12.ezyfox.morphia.EzyDataStoreBuilder;
import com.tvd12.ezyfox.morphia.bean.EzyMorphiaRepositories;
import com.tvd12.ezyfox.stream.EzyAnywayInputStreamLoader;

import dev.morphia.Datastore;

public class LocalMapAnimalPersistExample {

	@SuppressWarnings("rawtypes")
	public void test() {
		SimpleSettings settings = new SimpleSettings();
		SimpleEntityMapPersistSetting mapPersistSetting = new SimpleEntityMapPersistSetting();
		mapPersistSetting.setWriteDelay(0);
		SimpleEntityMapSetting mapSetting = new SimpleEntityMapSetting();
		mapSetting.setMapName(CollectionNames.ANIMAL);
		mapSetting.setPersistSetting(mapPersistSetting);
		settings.addMapSetting(mapSetting);
		EzyBeanContext beanContext = newBeanContext();
		DefaultEntityMapPersistFactory mapPersistFactory = new DefaultEntityMapPersistFactory();
		List mapPersistences = beanContext.getSingletons(MapPersistence.class);
		for(Object mapPersist : mapPersistences) {
			String mapName = MapPersistenceAnnotations.getMapName(mapPersist);
			mapPersistFactory.addMapPersist(mapName, (EntityMapPersist) mapPersist);
		}
		
		SimpleEntityUniqueFactory.Builder uniqueFactoryBuilder = SimpleEntityUniqueFactory.builder();
		UniqueKeyMapsBuilder<Animal> uniqueKeyMapsBuilder = uniqueFactoryBuilder.newUniqueKeyMapsBuilder(CollectionNames.ANIMAL);
		uniqueKeyMapsBuilder.addUniqueKeyMap("nick", a -> a.getNick());
		uniqueKeyMapsBuilder.build();
		EntityUniqueFactory uniqueFactory = uniqueFactoryBuilder.build();
		
		Calabash calabash = new CalabashBuilder()
				.settings(settings)
				.uniqueFactory(uniqueFactory)
				.mapPersistFactory(mapPersistFactory)
				.build();
		Animal animal = new Animal(1, "animal 1", "cat");
		EntityMap<Long, Animal> entityMap = calabash.getEntityMap(CollectionNames.ANIMAL);
//		entityMap.put(animal.getId(), animal);
		AnimalByNickQuery query = new AnimalByNickQuery(animal.getNick());
		Animal animalByQuery = entityMap.getByQuery(query);
		System.out.println("animal by query: " + animalByQuery);
		
	}
	
	protected EzyBeanContext newBeanContext() {
		MongoClient mongoClient = newMongoClient("mongo_config.properties");
		Datastore datastore = newDatastore(mongoClient, "test");
		EzyBeanContextBuilder builder = EzyBeanContext.builder()
				.addSingleton("mongoClient", mongoClient)
				.addSingleton("datastore", datastore)
				.scan("com.tvd12.calabash.local.test.mappersist");
		addAutoImplMongoRepo(builder, datastore);
		EzyBeanContext beanContext = builder.build();
		return beanContext;
	}
	
	private Datastore newDatastore(MongoClient mongoClient, String databaseName) {
		return EzyDataStoreBuilder.dataStoreBuilder()
			.mongoClient(mongoClient)
			.databaseName(databaseName)
			.scan("com.tvd12.calabash.local.test.mappersist")
			.build();
	}
	
	private MongoClient newMongoClient(String filePath) {
		MongoClient mongoClient = new EzyInputStreamMongoClientLoader()
				.inputStream(EzyAnywayInputStreamLoader.builder()
						.context(getClass())
						.build()
						.load("mongo_config.properties"))
				.load();
		Runtime.getRuntime().addShutdownHook(new Thread(() -> mongoClient.close()));
		return mongoClient;
	}
	
	private void addAutoImplMongoRepo(EzyBeanContextBuilder builder, Datastore datastore) {
		Map<Class<?>, Object> additionalRepo = implementMongoRepo(datastore);
		for (Class<?> repoType : additionalRepo.keySet()) {
			builder.addSingleton(getBeanName(repoType), additionalRepo.get(repoType));
		}
	}
	
	private Map<Class<?>, Object> implementMongoRepo(Datastore datastore) {
		return EzyMorphiaRepositories.newRepositoriesImplementer()
			.scan("com.tvd12.calabash.local.test.mappersist")
			.implement(datastore);
}
	
	public static void main(String[] args) {
		new LocalMapAnimalPersistExample().test();
	}
	
}
