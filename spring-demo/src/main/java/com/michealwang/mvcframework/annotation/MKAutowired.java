package com.michealwang.mvcframework.annotation;

import java.lang.annotation.*;

/**
 * 仿Spring自定义Autowired
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MKAutowired {
    String value() default "";
}
