package com.micheal.demo.temp.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author <a href="mailto:wangmk13@163.com">micheal.wang</a>
 * @date 2020/9/28 20:11
 * @Description
 * 如果我们要做一个线程池？无法做，因为Thread类里的target对象为执行的真正对象，而一个线程
 * 创建之后，调用start()之后除非我们停止它或者执行完毕或者抛出异常，才能停止
 */
public class ThreadPoolDemo {

    public static final Object LOCK = new Object();

    /**
     * 只能放10个， count=10，停止生产
     * count=0，消费者停止消费
     */
    static int repositry = 0;

    /**
     * 判断仓库是否为空
     * if empty return true then return false
     * @return
     */
    private static boolean isEmpty() {
        return repositry == 0 ? true : false;
    }

    /**
     * 判断仓库是否已满
     * if full return true then return false
     * @return
     */
    private static boolean isFull() {
        return repositry == 10 ? true : false;
    }

    static class Producer implements Runnable{

        @Override
        public void run() {
            while (true) {
                synchronized (LOCK) {
                    // 判断仓库是否已满
                    if (isFull()) {
                        try {
                            System.out.println("repositry: " + repositry);
                            // 再让自己阻塞之前先通知消费者消费
                            Thread.sleep(500);
                            LOCK.notify();
                            LOCK.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } else {
                        System.out.println(Thread.currentThread().getName() + ",仓库容量：" + repositry);
//                        LOCK.notify();
                        repositry++;
                    }
                }
            }
        }
    }

    static class Consumer implements Runnable{

        @Override
        public void run() {
            while (true) {
                synchronized (LOCK) {
                    if (isEmpty()) {
                        try {
                            System.out.println("repositry: " + repositry);
                            Thread.sleep(100);
                            LOCK.notify();
                            LOCK.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } else {
//                        LOCK.notify();
                        repositry--;
                    }
                }
                System.out.println(Thread.currentThread().getName() + ",仓库容量：" + repositry);
            }
        }
    }



    public static void main(String[] args) {
        new Thread(new Producer(), "生产者1").start();
        new Thread(new Consumer(), "消费者1").start();
        new Thread(new Producer(), "生产者2").start();
        new Thread(new Consumer(), "消费者2").start();
        new Thread(new Producer(), "生产者3").start();
        new Thread(new Consumer(), "消费者3").start();
        new Thread(new Producer(), "生产者4").start();
        new Thread(new Consumer(), "消费者4").start();

//        // 大神做法，做了哪些，里面经历了哪些？
//        // 提倡面向接口编程？为何？因为接口是高度抽象，符合业务的逻辑分析，可以通过接口来把
//        // 实际的业务给书写和构思出来，然后具体实现。
        ExecutorService newCachedThreadPool = Executors.newCachedThreadPool();
//        ExecutorService newFixedThreadPool = Executors.newFixedThreadPool(10);
//        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool();
//        ExecutorService newSingleThreadExecutor = Executors.newSingleThreadExecutor();
    }
}
