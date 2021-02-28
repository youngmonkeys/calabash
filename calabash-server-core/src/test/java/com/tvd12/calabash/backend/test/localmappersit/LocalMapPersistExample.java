package com.tvd12.calabash.backend.test.localmappersit;

import java.util.List;

import com.tvd12.calabash.Calabash;
import com.tvd12.calabash.backend.test.ServerCoreBaseTest;
import com.tvd12.calabash.core.BytesMap;
import com.tvd12.calabash.core.EntityMapPersist;
import com.tvd12.calabash.core.annotation.MapPersistence;
import com.tvd12.calabash.core.util.ByteArray;
import com.tvd12.calabash.core.util.MapPersistenceAnnotations;
import com.tvd12.calabash.persist.factory.DefaultEntityMapPersistFactory;
import com.tvd12.calabash.server.core.CalabashServerContext;
import com.tvd12.calabash.server.core.setting.SimpleMapPersistSetting;
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
		DefaultEntityMapPersistFactory mapPersistFactory = new DefaultEntityMapPersistFactory();
		List mapPersistences = beanContext.getSingletons(MapPersistence.class);
		for(Object mapPersist : mapPersistences) {
			String mapName = MapPersistenceAnnotations.getMapName(mapPersist);
			mapPersistFactory.addMapPersist(mapName, (EntityMapPersist) mapPersist);
		}
		Calabash calabash = CalabashServerContext.builder()
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
