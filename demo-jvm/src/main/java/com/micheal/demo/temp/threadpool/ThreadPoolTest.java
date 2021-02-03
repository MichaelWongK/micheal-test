package com.micheal.demo.temp.threadpool;

import java.util.concurrent.*;

/**
 * @author <a href="mailto:wangmk13@163.com">micheal.wang</a>
 * @date 2021/1/11 15:28
 * @Description
 */
public class ThreadPoolTest {

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newCachedThreadPool(); // 快
        ExecutorService executorService1 = Executors.newFixedThreadPool(100); // 慢
        ExecutorService executorService2 = Executors.newSingleThreadExecutor(); // 最慢

        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(10, 20,
                0L, TimeUnit.MILLISECONDS,
                new ArrayBlockingQueue<Runnable>(10));

        for (int i = 0; i < 100; i++) {
            threadPoolExecutor.execute(new MyTask(i));
        }
    }
}

class MyTask implements Runnable {

    int i = 0;

    public MyTask(int i) {
        this.i = i;
    }

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + "--" + i);
        try {
//            Thread.sleep(1000); // 业务逻辑
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
