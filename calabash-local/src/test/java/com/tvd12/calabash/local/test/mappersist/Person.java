package com.tvd12.calabash.local.test.mappersist;

import com.tvd12.calabash.local.test.CollectionNames;

import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity(CollectionNames.PERSON)
public class Person {

	@Id
	protected long id;
	protected String name;
	protected int age;
	
}
