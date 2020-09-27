package com.micheal.demo.temp.jvm;

/**
 * @author <a href="mailto:wangmk13@163.com">micheal.wang</a>
 * @date 2020/9/12 22:44
 * @Description
 */
class B {
    public void out() {
        System.out.println("this is B out method");
    }
}
public class DemoType extends B{

    public void out() {
        System.out.println("this is Demo out method");
    }

    public static void main(String[] args) {
        B b = new DemoType();
        b.out();
    }
}
