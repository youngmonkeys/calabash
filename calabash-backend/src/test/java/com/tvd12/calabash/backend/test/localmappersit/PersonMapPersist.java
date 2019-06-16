package com.tvd12.calabash.backend.test.localmappersit;

import java.util.Map;

import com.tvd12.calabash.backend.annotation.MapPersistence;
import com.tvd12.calabash.core.EntityMapPersist;
import com.tvd12.ezyfox.bean.annotation.EzyAutoBind;
import com.tvd12.ezyfox.bean.annotation.EzySingleton;

import lombok.Setter;

@Setter
@EzySingleton
@MapPersistence(CollectionNames.PERSON)
public class PersonMapPersist implements EntityMapPersist<Long, Person> {

	@EzyAutoBind
	protected PersonRepo personRepo;
	
	@Override
	public Person load(Long key) {
		Person person = personRepo.findById(key);
		return person;
	}

	@Override
	public void persist(Long key, Person value) {
		personRepo.save(value);
	}

	@Override
	public void persist(Map<Long, Person> m) {
		personRepo.save(m.values());
	}

	

}
