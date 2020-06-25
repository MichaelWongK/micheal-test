package com.micheal.demo.temp.domain;

public class Person {

    private String name;

    private int age;

    private String address;

//    private Person(String name) {
//        this.name = name;
//    }
//
//    public Person(String name, int age, String address) {
//        this.name = name;
//        this.age = age;
//        this.address = address;
//    }

    public void eat(String s) {
        System.out.println("调用了：公有的，String参数的eat(): s = " + s);
    }

    protected void paly() {
        System.out.println("调用了：受保护的，无参的paly()");
    }

    void run() {
        System.out.println("调用了：默认的，无参的run()");
    }

    private String study(int age) {
        System.out.println("调用了，私有的，并且有返回值的，int参数的study(): age = " + age);
        return "abcd";
    }

}
