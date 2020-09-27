package com.micheal.demo.temp.innerclass;

/**
 * @author <a href="mailto:wangmk13@163.com">micheal.wang</a>
 * @date 2020/9/14 23:34
 * @Description
 */
class A {
    public void print() {
        System.out.println("A print");
    }
}
public class Hello {
    public static void main(String[] args) {
        new A() {
            public void print() {
                System.out.println("Hello A print");
            }
        }.print();
    }
}
