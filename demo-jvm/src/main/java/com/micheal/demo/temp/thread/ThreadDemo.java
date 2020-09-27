package com.micheal.demo.temp.thread;

import org.junit.Test;

/**
 * @author <a href="mailto:wangmk13@163.com">micheal.wang</a>
 * @date 2020/9/20 13:44
 * @Description
 * 线程同步 想到的是synchronized枷锁，那么它的原理是啥
 * 我们如果synchronzied加在静态方法上，那么我们在字节码中无法看到它锁了，事实上他是锁了整个类，也就是class
 *
 */
public class ThreadDemo {

    public static int count;

    public static synchronized void exec() {
        count+=1;
    }

    @Test
    public void test() {
        for (int i=0; i<1000; i++) {
            Thread thread = new Thread(new MyRun());
            thread.start();
        }

        System.out.println(count);
    }

    static class MyRun implements Runnable {

        @Override
        public void run() {
            exec();
        }
    }
}
