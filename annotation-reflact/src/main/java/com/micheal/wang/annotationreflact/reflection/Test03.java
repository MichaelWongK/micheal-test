package com.micheal.wang.annotationreflact.reflection;

public class Test03 {

    public static void main(String[] args) throws ClassNotFoundException {
        Person person = new Student();
        System.out.println("这个人是：" + person.name);

        Class c1 = person.getClass();
        Class c2 = Class.forName("com.micheal.wang.annotationreflact.reflection.Student");
        System.out.println(c1.hashCode());
        System.out.println(c2.hashCode());

        Class<Student> c3 = Student.class;
        System.out.println(c3.hashCode());

        Class<Integer> c4 = Integer.TYPE;
        System.out.println(c4);

        Class superclass = c1.getSuperclass();
        System.out.println(superclass);
    }

}

class Person {
    public String name;

    public Person() {

    }

    public Person(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                '}';
    }

}

class Student extends Person {
    public Student() {
        this.name = "学生";
    }
}

class Teacher extends Person {
    public Teacher() {
        this.name = "老师";
    }
}