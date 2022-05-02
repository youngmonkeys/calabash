package com.tvd12.calabash.client.annotation;

import java.lang.annotation.*;

@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface CachedValue {

    String mapName() default "";

    String value() default "";

    Class<?> keyType() default Object.class;
}
