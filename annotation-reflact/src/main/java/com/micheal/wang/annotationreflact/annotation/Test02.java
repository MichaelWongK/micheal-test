package com.micheal.wang.annotationreflact.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public class Test02 {

    // 注解可以显示赋值， 如果没有默认值， 必须给注解赋值
    @Myannotation(name = "micheal", age = 18)
    public void test() {

    }
}

@Target(value = {ElementType.METHOD, ElementType.TYPE})
@Retention(value = RetentionPolicy.RUNTIME)
@interface Myannotation{
    // 注解的参数：参数类型 + 参数名（）
    String name() default "";

    int age();

    int id() default -1; // 默认-1代表不存在

    String[] schools() default {"北京大学", "南京大学"};
}