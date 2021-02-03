package com.micheal.demo.temp.thread.lock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * @author <a href="mailto:wangmk13@163.com">micheal.wang</a>
 * @date 2021/2/3 15:48
 * @Description AQS
 */
public class MkLock extends AbstractQueuedSynchronizer implements Lock {
    @Override
    public void lock() {
        int state = getState();
        if (0 == state) {
            if (compareAndSetState(0, 1)) {
                setExclusiveOwnerThread(Thread.currentThread());

                System.out.println(Thread.currentThread().getName() + ": 抢占锁成功");
                return;
            }
        } else if (Thread.currentThread() == getExclusiveOwnerThread()){
            // 重入   getExclusiveOwnerThread() 获得独占的线程与当前线程比较

        } else {
            // 抢锁失败

            // 入队

            // 阻塞
        }

        System.out.println(Thread.currentThread().getName() + ": 抢占锁失败");
        addQueue();
    }

    /**
     * 加入队列（存在并发）
     */
    private void addQueue() {
        new AbstractQueuedSynchronizer.Node(Thread.currentThread(), AbstractQueuedSynchronizer().Node.EXCLUSIVE);
        AbstractQueuedSynchronizer.Node.EXCLUSIVE;new AbstractQueuedSynchronizer.Node();
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {

    }

    @Override
    public boolean tryLock() {
        return false;
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return false;
    }

    @Override
    public void unlock() {

    }

    @Override
    public Condition newCondition() {
        return null;
    }
}
