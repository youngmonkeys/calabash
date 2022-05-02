package com.tvd12.calabash.core.setting;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SimpleMapEvictionSetting implements MapEvictionSetting {

    protected int keyMaxIdleTime;
}
