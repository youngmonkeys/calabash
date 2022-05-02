package com.tvd12.calabash.backend.test.localmappersit;

import com.tvd12.calabash.persist.EntityMapPersist;
import com.tvd12.calabash.persist.annotation.MapPersistence;
import com.tvd12.ezyfox.bean.annotation.EzyAutoBind;
import com.tvd12.ezyfox.bean.annotation.EzySingleton;
import lombok.Setter;

import java.util.Map;

@Setter
@EzySingleton
@MapPersistence(CollectionNames.PERSON)
public class PersonMapPersist implements EntityMapPersist<Long, Person> {

    @EzyAutoBind
    protected PersonRepo personRepo;

    @Override
    public Person load(Long key) {
        return personRepo.findById(key);
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
