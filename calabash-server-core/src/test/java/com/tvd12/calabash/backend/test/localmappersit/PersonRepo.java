package com.tvd12.calabash.backend.test.localmappersit;

import com.tvd12.ezydata.mongodb.EzyMongoRepository;
import com.tvd12.ezyfox.annotation.EzyAutoImpl;

@EzyAutoImpl
public interface PersonRepo extends EzyMongoRepository<Long, Person> {
}
