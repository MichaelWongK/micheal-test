package com.micheal.wang.annotationreflact.reflection;


public class Test02 extends Object{
    public static void main(String[] args) throws ClassNotFoundException {
        Class cl = Class.forName("com.micheal.wang.annotationreflact.reflection.User");
        System.out.println(cl);

        Class cl2 = Class.forName("com.micheal.wang.annotationreflact.reflection.User");
        Class cl3 = Class.forName("com.micheal.wang.annotationreflact.reflection.User");
        Class cl4 = Class.forName("com.micheal.wang.annotationreflact.reflection.User");
        System.out.println(cl2.hashCode());
        System.out.println(cl3.hashCode());
        System.out.println(cl4.hashCode());

    }
}
class User {

    String name;
    int age;
    int id;

    public User() {

    }

    public User(String name, int age, int id) {
        this.name = name;
        this.age = age;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
