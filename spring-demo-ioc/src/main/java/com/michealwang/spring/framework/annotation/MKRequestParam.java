package com.michealwang.spring.framework.annotation;

import java.lang.annotation.*;

@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MKRequestParam {
    String value() default "";
}
