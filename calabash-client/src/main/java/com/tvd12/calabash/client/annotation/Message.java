package com.tvd12.calabash.client.annotation;

import java.lang.annotation.*;

@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Message {

    String channel() default "";

    String value() default "";
}
