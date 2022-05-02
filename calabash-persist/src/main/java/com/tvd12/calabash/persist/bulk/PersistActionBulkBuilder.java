package com.tvd12.calabash.persist.bulk;

import com.tvd12.calabash.persist.action.PersistAction;
import com.tvd12.ezyfox.builder.EzyBuilder;

import java.util.List;

public interface PersistActionBulkBuilder extends EzyBuilder<PersistActionBulk> {

    PersistActionBulkBuilder mapPersist(Object mapPersist);

    PersistActionBulkBuilder actions(List<PersistAction> actions);
}
