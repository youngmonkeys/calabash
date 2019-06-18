package com.tvd12.calabash.backend.persist;

import java.util.List;

import com.tvd12.calabash.backend.BytesMapPersist;
import com.tvd12.calabash.core.persist.PersistAction;
import com.tvd12.calabash.core.persist.PersistActionBulk;
import com.tvd12.ezyfox.builder.EzyBuilder;

public interface PersistActionBulkBuilder extends EzyBuilder<PersistActionBulk> {

	PersistActionBulkBuilder mapPersist(BytesMapPersist mapPersist);
	
	PersistActionBulkBuilder actions(List<PersistAction> actions);
	
}
