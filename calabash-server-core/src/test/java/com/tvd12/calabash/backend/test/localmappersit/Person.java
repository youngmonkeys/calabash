package com.tvd12.calabash.backend.test.localmappersit;

import com.tvd12.ezydata.database.annotation.EzyCollection;
import com.tvd12.ezyfox.annotation.EzyId;
import com.tvd12.ezyfox.binding.annotation.EzyArrayBinding;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@EzyArrayBinding
@NoArgsConstructor
@AllArgsConstructor
@EzyCollection(CollectionNames.PERSON)
public class Person {

	@EzyId
	protected long id;
	protected String name;
	protected int age;
	
}
