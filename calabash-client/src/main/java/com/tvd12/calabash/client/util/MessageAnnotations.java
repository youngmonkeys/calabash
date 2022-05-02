package com.tvd12.calabash.client.util;

import com.tvd12.calabash.client.annotation.Message;
import com.tvd12.ezyfox.io.EzyStrings;

public final class MessageAnnotations {

    private MessageAnnotations() {}

    public static String getChannelName(Class<?> messageClass) {
        return getChannelName(messageClass.getAnnotation(Message.class));
    }

    public static String getChannelName(Message anno) {
        String channelName = anno.value();
        if (EzyStrings.isNoContent(channelName)) {
            channelName = anno.channel();
        }
        return channelName;
    }
}
