package com.tvd12.calabash.client.concurrent;

import com.tvd12.ezyfox.concurrent.EzyThreadFactory;

public class CalabashThreadFactory extends EzyThreadFactory {

    protected CalabashThreadFactory(Builder builder) {
        super(builder);
    }

    public static EzyThreadFactory create(String poolName) {
        return builder().poolName(poolName).build();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends EzyThreadFactory.Builder {

        protected Builder() {
            super();
            this.prefix = "ezydata-redis";
        }

        @Override
        public EzyThreadFactory build() {
            return new CalabashThreadFactory(this);
        }
    }
}
