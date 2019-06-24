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
@Entity(CollectionNames.ANIMAL)
public class Animal {

	@Id
	protected long id;
	protected String nick;
	protected String name;
	
}
