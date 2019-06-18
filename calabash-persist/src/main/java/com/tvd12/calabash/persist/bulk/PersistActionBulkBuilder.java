package com.tvd12.calabash.persist.bulk;

import java.util.List;

import com.tvd12.calabash.persist.action.PersistAction;
import com.tvd12.ezyfox.builder.EzyBuilder;

public interface PersistActionBulkBuilder extends EzyBuilder<PersistActionBulk> {

	PersistActionBulkBuilder mapPersist(Object mapPersist);
	
	PersistActionBulkBuilder actions(List<PersistAction> actions);
	
}
