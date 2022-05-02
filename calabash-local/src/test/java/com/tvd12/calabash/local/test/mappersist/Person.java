package com.tvd12.calabash.local.test.mappersist;

import com.tvd12.calabash.local.test.CollectionNames;
import com.tvd12.ezyfox.annotation.EzyId;
import com.tvd12.ezyfox.database.annotation.EzyCollection;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EzyCollection(CollectionNames.PERSON)
public class Person {
    @EzyId
    protected long id;
    protected String name;
    protected int age;
}
