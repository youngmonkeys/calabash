package com.tvd12.calabash.local.setting;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SimpleEntityMapEvictionSetting implements EntityMapEvictionSetting {

    protected int keyMaxIdleTime;
}
