package com.tvd12.calabash.client.setting;

import com.tvd12.ezyfox.builder.EzyBuilder;
import com.tvd12.ezyfox.reflect.EzyClasses;

public class MessageChannelSettingBuilder implements EzyBuilder<MessageChannelSetting> {

    protected Class<?> messageType;
    protected int subThreadPoolSize = 1;
    protected SettingsBuilder parent;

    public MessageChannelSettingBuilder() {}

    public MessageChannelSettingBuilder(SettingsBuilder parent) {
        this.parent = parent;
    }

    public MessageChannelSettingBuilder messageType(Class<?> messageType) {
        this.messageType = messageType;
        return this;
    }

    public MessageChannelSettingBuilder messageType(String messageType) {
        if (messageType != null) {
            this.messageType = EzyClasses.getClass(messageType);
        }
        return this;
    }

    public MessageChannelSettingBuilder subThreadPoolSize(int subThreadPoolSize) {
        this.subThreadPoolSize = subThreadPoolSize;
        return this;
    }

    public MessageChannelSettingBuilder subThreadPoolSize(String subThreadPoolSize) {
        if (subThreadPoolSize != null) {
            this.subThreadPoolSize = Integer.parseInt(subThreadPoolSize);
        }
        return this;
    }

    public SettingsBuilder parent() {
        return parent;
    }

    @Override
    public MessageChannelSetting build() {
        SimpleChannelSetting setting = new SimpleChannelSetting();
        setting.setMessageType(messageType);
        setting.setSubThreadPoolSize(subThreadPoolSize);
        return setting;
    }
}
