package com.micheal.demo.temp.thread.lock;


/**
 * @author <a href="mailto:wangmk13@163.com">micheal.wang</a>
 * @date 2021/2/2 14:12
 * @Description
 */
public class ThreadTest_2 {

    static int val = 0;

    public static void main(String[] args) {
        Thread t1 = new Thread(() -> count());

        Thread t2 = new Thread(() -> count());

        Thread t3 = new Thread(() -> count());

        t1.start();
        t2.start();
        t3.start();

        try {
            t1.join();
            t3.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println(val);
    }

    static synchronized void count1() {

        for (int i = 0; i < 10000; i++) {
            val++;
        }
    }

    static void count() {
        synchronized(ThreadTest_2.class) {
            for (int i = 0; i < 10000; i++) {
                val++;
            }
        }
    }
}
