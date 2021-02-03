package com.micheal.demo.temp.thread.lock;

import java.util.concurrent.locks.Lock;

/**
 * @author <a href="mailto:wangmk13@163.com">micheal.wang</a>
 * @date 2021/2/2 14:12
 * @Description
 */
public class ThreadTest_1 {

    static int val = 0;

    static Lock lock = new MkLock();

    public static void main(String[] args) {
        Thread t1 = new Thread(() -> count());

        Thread t2 = new Thread(() -> count());

        Thread t3 = new Thread(() -> count());

        t1.start();
        t2.start();
        t3.start();

        try {
            t1.join();
            t2.join();
            t3.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println(val);
    }

    private static void count() {
        try {
            lock.lock();

            for (int i = 0; i < 10000; i++) {
                val++;
            }
        } finally {
            lock.unlock();
        }
    }
}
