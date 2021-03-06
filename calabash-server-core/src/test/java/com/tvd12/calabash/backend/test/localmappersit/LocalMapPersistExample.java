package com.tvd12.calabash.backend.test.localmappersit;

import java.util.List;

import com.tvd12.calabash.Calabash;
import com.tvd12.calabash.backend.test.ServerCoreBaseTest;
import com.tvd12.calabash.core.BytesMap;
import com.tvd12.calabash.core.util.ByteArray;
import com.tvd12.calabash.persist.EntityMapPersist;
import com.tvd12.calabash.persist.annotation.MapPersistence;
import com.tvd12.calabash.persist.factory.BytesMapPersistFactory;
import com.tvd12.calabash.persist.factory.EntityBytesMapPersistFactory;
import com.tvd12.calabash.persist.factory.SimpleEntityMapPersistFactory;
import com.tvd12.calabash.persist.setting.SimpleMapPersistSetting;
import com.tvd12.calabash.persist.util.MapPersistenceAnnotations;
import com.tvd12.calabash.server.core.CalabashServerContext;
import com.tvd12.calabash.server.core.setting.SimpleMapSetting;
import com.tvd12.calabash.server.core.setting.SimpleSettings;
import com.tvd12.ezyfox.bean.EzyBeanContext;
import com.tvd12.ezyfox.binding.EzyBindingContext;
import com.tvd12.ezyfox.binding.codec.EzyBindingEntityCodec;
import com.tvd12.ezyfox.codec.EzyEntityCodec;
import com.tvd12.ezyfox.codec.EzyMessageDeserializer;
import com.tvd12.ezyfox.codec.EzyMessageSerializer;
import com.tvd12.ezyfox.codec.MsgPackSimpleDeserializer;
import com.tvd12.ezyfox.codec.MsgPackSimpleSerializer;

public class LocalMapPersistExample extends ServerCoreBaseTest {

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
		SimpleEntityMapPersistFactory.Builder mapPersistFactoryBuilder = 
				SimpleEntityMapPersistFactory.builder();
		List mapPersistences = beanContext.getSingletons(MapPersistence.class);
		for(Object mapPersist : mapPersistences) {
			String mapName = MapPersistenceAnnotations.getMapName(mapPersist);
			mapPersistFactoryBuilder.addMapPersist(mapName, (EntityMapPersist) mapPersist);
		}
		BytesMapPersistFactory bytesMapPersistFactory = EntityBytesMapPersistFactory.builder()
				.entityCodec(entityCodec)
				.entityMapPersistFactory(mapPersistFactoryBuilder.build())
				.build();
		Calabash calabash = CalabashServerContext.builder()
				.settings(settings)
				.bytesMapPersistFactory(bytesMapPersistFactory)
				.build();
		ByteArray keyBytes = new ByteArray(entityCodec.serialize(1L));
		byte[] values = entityCodec.serialize(new Person(9L, "bar", 29));
		BytesMap bytesMap = calabash.getBytesMap(CollectionNames.PERSON);
		bytesMap.put(keyBytes, values);
		
	}
	
	protected EzyEntityCodec newEntityCodec() {
		EzyBindingContext bindingContext = EzyBindingContext.builder()
				.scan("com.tvd12.calabash.server.core.test.localmappersit")
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
	
	public static void main(String[] args) {
		new LocalMapPersistExample().test();
	}
	
}
