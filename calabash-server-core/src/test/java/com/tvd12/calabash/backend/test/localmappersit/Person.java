package com.tvd12.calabash.backend.test.localmappersit;

import com.tvd12.ezyfox.binding.annotation.EzyArrayBinding;

import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@EzyArrayBinding
@NoArgsConstructor
@AllArgsConstructor
@Entity(CollectionNames.PERSON)
public class Person {

	@Id
	protected long id;
	protected String name;
	protected int age;
	
}
