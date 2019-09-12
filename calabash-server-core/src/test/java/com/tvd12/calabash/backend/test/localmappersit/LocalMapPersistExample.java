package com.tvd12.calabash.backend.test.localmappersit;

import static com.tvd12.ezyfox.util.EzyAutoImplAnnotations.getBeanName;

import java.util.List;
import java.util.Map;

import com.mongodb.MongoClient;
import com.tvd12.calabash.Calabash;
import com.tvd12.calabash.backend.builder.CalabashBuilder;
import com.tvd12.calabash.backend.setting.SimpleMapPersistSetting;
import com.tvd12.calabash.backend.setting.SimpleMapSetting;
import com.tvd12.calabash.backend.setting.SimpleSettings;
import com.tvd12.calabash.core.BytesMap;
import com.tvd12.calabash.core.EntityMapPersist;
import com.tvd12.calabash.core.annotation.MapPersistence;
import com.tvd12.calabash.core.util.ByteArray;
import com.tvd12.calabash.core.util.MapPersistenceAnnotations;
import com.tvd12.calabash.persist.factory.DefaultEntityMapPersistFactory;
import com.tvd12.ezyfox.bean.EzyBeanContext;
import com.tvd12.ezyfox.bean.EzyBeanContextBuilder;
import com.tvd12.ezyfox.binding.EzyBindingContext;
import com.tvd12.ezyfox.binding.codec.EzyBindingEntityCodec;
import com.tvd12.ezyfox.codec.EzyEntityCodec;
import com.tvd12.ezyfox.codec.EzyMessageDeserializer;
import com.tvd12.ezyfox.codec.EzyMessageSerializer;
import com.tvd12.ezyfox.codec.MsgPackSimpleDeserializer;
import com.tvd12.ezyfox.codec.MsgPackSimpleSerializer;
import com.tvd12.ezyfox.mongodb.loader.EzyInputStreamMongoClientLoader;
import com.tvd12.ezyfox.morphia.EzyDataStoreBuilder;
import com.tvd12.ezyfox.morphia.bean.EzyMorphiaRepositories;
import com.tvd12.ezyfox.stream.EzyAnywayInputStreamLoader;

import dev.morphia.Datastore;

public class LocalMapPersistExample {

	@SuppressWarnings("rawtypes")
	public void test() {
		EzyEntityCodec entityCodec = newEntityCodec();
		SimpleSettings settings = new SimpleSettings();
		SimpleMapPersistSetting mapPersistSetting = new SimpleMapPersistSetting();
		SimpleMapSetting mapSetting = new SimpleMapSetting();
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
				.entityCodec(entityCodec)
				.entityMapPersistFactory(mapPersistFactory)
				.build();
		ByteArray keyBytes = new ByteArray(entityCodec.serialize(1L));
		byte[] values = entityCodec.serialize(new Person(9L, "bar", 29));
		BytesMap bytesMap = calabash.getBytesMap(CollectionNames.PERSON);
		bytesMap.put(keyBytes, values);
		
	}
	
	protected EzyEntityCodec newEntityCodec() {
		EzyBindingContext bindingContext = EzyBindingContext.builder()
				.scan("com.tvd12.calabash.backend.test.localmappersit")
				.build();
		EzyMessageSerializer messageSerializer = new MsgPackSimpleSerializer();
		EzyMessageDeserializer messageDeserializer = new MsgPackSimpleDeserializer();
		EzyEntityCodec entityCodec = EzyBindingEntityCodec.builder()
				.marshaller(bindingContext.newMarshaller())
				.unmarshaller(bindingContext.newUnmarshaller())
				.messageSerializer(messageSerializer)
				.messageDeserializer(messageDeserializer)
				.build();
		return entityCodec;
	}
	
	protected EzyBeanContext newBeanContext() {
		MongoClient mongoClient = newMongoClient("mongo_config.properties");
		Datastore datastore = newDatastore(mongoClient, "test");
		EzyBeanContextBuilder builder = EzyBeanContext.builder()
				.addSingleton("mongoClient", mongoClient)
				.addSingleton("datastore", datastore)
				.scan("com.tvd12.calabash.backend.test.localmappersit");
		addAutoImplMongoRepo(builder, datastore);
		EzyBeanContext beanContext = builder.build();
		return beanContext;
	}
	
	private Datastore newDatastore(MongoClient mongoClient, String databaseName) {
		return EzyDataStoreBuilder.dataStoreBuilder()
			.mongoClient(mongoClient)
			.databaseName(databaseName)
			.scan("com.tvd12.calabash.backend.test.localmappersit")
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
			.scan("com.tvd12.calabash.backend.test.localmappersit")
			.implement(datastore);
}
	
	public static void main(String[] args) {
		new LocalMapPersistExample().test();
	}
	
}
