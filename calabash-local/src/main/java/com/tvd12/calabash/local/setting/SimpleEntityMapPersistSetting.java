package com.tvd12.calabash.local.setting;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SimpleEntityMapPersistSetting implements EntityMapPersistSetting {

    protected long writeDelay = 1000L;
}
