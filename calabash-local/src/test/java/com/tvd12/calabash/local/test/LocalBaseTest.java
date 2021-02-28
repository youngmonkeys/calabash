package com.tvd12.calabash.local.test;

import java.util.Map;

import com.mongodb.MongoClient;
import com.tvd12.ezydata.mongodb.EzyMongoDatabaseContextBuilder;
import com.tvd12.ezydata.mongodb.loader.EzySimpleMongoClientLoader;
import com.tvd12.ezyfox.bean.EzyBeanContext;
import com.tvd12.ezyfox.bean.EzyBeanContextBuilder;
import com.tvd12.ezyfox.stream.EzyAnywayInputStreamLoader;

public class LocalBaseTest {

	protected EzyBeanContext newBeanContext() {
		MongoClient mongoClient = newMongoClient("mongo_config.properties");
		EzyBeanContextBuilder builder = EzyBeanContext.builder()
				.addSingleton("mongoClient", mongoClient)
				.scan("com.tvd12.calabash.local.test.mappersist");
		addAutoImplMongoRepo(builder, mongoClient);
		EzyBeanContext beanContext = builder.build();
		return beanContext;
	}
	
	private void addAutoImplMongoRepo(
			EzyBeanContextBuilder builder, MongoClient mongoClient) {
		Map<String, Object> additionalRepo = new EzyMongoDatabaseContextBuilder()
				.mongoClient(mongoClient)
				.propertiesFile("mongo_config.properties")
				.scan("com.tvd12.calabash.local.test.mappersist")
				.build()
				.getRepositoriesByName();
		for (String repoName : additionalRepo.keySet())
			builder.addSingleton(repoName, additionalRepo.get(repoName));
	}
	
	private MongoClient newMongoClient(String filePath) {
		MongoClient mongoClient = new EzySimpleMongoClientLoader()
				.inputStream(EzyAnywayInputStreamLoader.builder()
						.context(getClass())
						.build()
						.load("mongo_config.properties"))
				.load();
		Runtime.getRuntime().addShutdownHook(new Thread(() -> mongoClient.close()));
		return mongoClient;
	}
	
}
