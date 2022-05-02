package com.tvd12.calabash.client.annotation;

import java.lang.annotation.*;

@Documented
@Target({ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface CachedKey {

    boolean composite() default false;
}
