package com.tvd12.calabash.local.test.mappersist;

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
@Entity("___calabash_atomic_long___")
public class MaxId {

	@Id
	protected String id;
	protected long value;
	
}
