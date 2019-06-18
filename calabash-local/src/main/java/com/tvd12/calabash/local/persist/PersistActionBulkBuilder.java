package com.tvd12.calabash.local.persist;

import java.util.List;

import com.tvd12.calabash.core.EntityMapPersist;
import com.tvd12.calabash.core.persist.PersistAction;
import com.tvd12.calabash.core.persist.PersistActionBulk;
import com.tvd12.ezyfox.builder.EzyBuilder;

@SuppressWarnings("rawtypes")
public interface PersistActionBulkBuilder extends EzyBuilder<PersistActionBulk> {

	PersistActionBulkBuilder mapPersist(EntityMapPersist mapPersist);
	
	PersistActionBulkBuilder actions(List<PersistAction> actions);
	
}
