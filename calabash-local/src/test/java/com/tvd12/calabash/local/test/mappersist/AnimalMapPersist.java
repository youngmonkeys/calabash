package com.tvd12.calabash.local.test.mappersist;

import com.tvd12.calabash.local.test.AnimalByNickQuery;
import com.tvd12.calabash.local.test.CollectionNames;
import com.tvd12.calabash.persist.EntityMapPersist;
import com.tvd12.calabash.persist.annotation.MapPersistence;
import com.tvd12.ezyfox.bean.annotation.EzyAutoBind;
import com.tvd12.ezyfox.bean.annotation.EzySingleton;
import lombok.Setter;

import java.util.Map;

@Setter
@EzySingleton
@MapPersistence(CollectionNames.ANIMAL)
public class AnimalMapPersist implements EntityMapPersist<Long, Animal> {

    @EzyAutoBind
    protected AnimalRepo animalRepo;

    @Override
    public Animal load(Long key) {
        Animal entity = animalRepo.findById(key);
        return entity;
    }

    @Override
    public Object loadByQuery(Object query) {
        AnimalByNickQuery q = (AnimalByNickQuery) query;
        return animalRepo.findByField("nick", q.getNick());
    }

    @Override
    public void persist(Long key, Animal value) {
        animalRepo.save(value);
    }

    @Override
    public void persist(Map<Long, Animal> m) {
        animalRepo.save(m.values());
    }
}
