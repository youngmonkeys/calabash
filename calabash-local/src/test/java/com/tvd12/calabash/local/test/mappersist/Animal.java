package com.tvd12.calabash.local.test.mappersist;

import com.tvd12.calabash.core.prototype.Prototype;
import com.tvd12.calabash.local.test.CollectionNames;
import com.tvd12.ezydata.database.annotation.EzyCollection;
import com.tvd12.ezyfox.annotation.EzyId;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EzyCollection(CollectionNames.ANIMAL)
public class Animal implements Prototype {

	@EzyId
	protected long id;
	protected String nick;
	protected String name;
	
	@Override
	public Object clone() {
		Animal c = new Animal();
		c.id = id;
		c.nick = nick;
		c.name = name;
		return c;
	}
	
}
