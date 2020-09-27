package com.micheal.demo.temp;

/**
 * @author <a href="mailto:wangmk13@163.com">micheal.wang</a>
 * @date 2020/9/17 0:45
 * @Description
 */
public class B {

    public static B t1 = new B("t1");
    public static B t2 = new B("t2");
    public B(String str) {
        System.out.println(str);
    }
    {
        System.out.println("构造块");
    }
    static
    {
        System.out.println("静态块");
    }



    public static void main(String[] args) {
        B t = new B("init");
    }


}
