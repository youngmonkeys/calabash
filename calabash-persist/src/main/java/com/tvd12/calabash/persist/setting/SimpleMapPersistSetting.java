package com.tvd12.calabash.persist.setting;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SimpleMapPersistSetting implements MapPersistSetting {

    protected long writeDelay = 1000L;
}
