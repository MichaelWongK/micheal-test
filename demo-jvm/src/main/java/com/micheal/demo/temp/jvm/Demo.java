package com.micheal.demo.temp.jvm;


/**
 * @author <a href="mailto:wangmk13@163.com">micheal.wang</a>
 * @date 2020/9/12 18:41
 * @Description
 */

class A {
//    public void publicMethod() {
//        privateMethod();
//    }
    protected void protectedMethod() {
        privateMethod();
    }
    private void privateMethod() {
        System.out.println(1);
    }
}
public class Demo extends A{

    public static void main(String[] args) {
//        new Demo().publicMethod();
        new Demo().protectedMethod();
    }


}


