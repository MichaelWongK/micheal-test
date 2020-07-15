package com.michealwang.spring.framework.annotation;

import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MKRequestMapping {
    String value() default "";
}
