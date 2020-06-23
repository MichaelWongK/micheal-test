package com.micheal.demo.temp.reflect;

import com.micheal.demo.temp.domain.Person;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;

public class ClassTest {

    public static void main(String[] args) {
//        Person person = new Person("dog", 11, "北京");
//        Class clazz = person.getClass();
        Class clazz = null;
        try {
            clazz = Class.forName("com.micheal.demo.temp.domain.Person");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        System.out.println("获取到的对象：" + clazz);
        System.out.println("====================methods=================");
        Method[] methods = clazz.getMethods();
        Arrays.stream(methods).forEach(method -> System.out.println(method));

        try {
            Method m = clazz.getMethod("eat", String.class);
            System.out.println("=========m========");
            System.out.println(m);
            Object obj = clazz.getConstructor().newInstance();
            m.invoke(obj, "666");
            System.out.println("获取私有的study()方法");
            m = clazz.getDeclaredMethod("study", int.class);
            System.out.println(m);


            m.setAccessible(true);//解除私有限定
            Object result = m.invoke(obj, 20);//需要两个参数，一个是要调用的对象（获取有反射），一个是实参
            System.out.println("返回值：" + result);
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }

        HashMap
                ConnCurrenHashMap

//        System.out.println("====================fields=================");
//        Field[] fields = clazz.getDeclaredFields();
//        Arrays.stream(fields).forEach(field -> System.out.println(field));
//        try {
//            System.out.println("field:" + clazz.getDeclaredField("name"));
//        } catch (NoSuchFieldException e) {
//            e.printStackTrace();
//        }
//        System.out.println("====================constructors=================");
//        Constructor[] constructors = clazz.getDeclaredConstructors();
//        Arrays.stream(constructors).forEach(constructor -> System.out.println(constructor));

    }
}
