package com.tvd12.calabash.local.test.mappersist;

import com.tvd12.ezydata.mongodb.EzyMongoRepository;
import com.tvd12.ezyfox.annotation.EzyAutoImpl;

@EzyAutoImpl
public interface PersonRepo extends EzyMongoRepository<Long, Person> {}
