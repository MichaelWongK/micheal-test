package com.micheal.wang.redisdemo.service;

import com.micheal.wang.redisdemo.util.RedisLock;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 模拟秒杀
 */
public class SecKillService {

    @Autowired
    RedisLock redisLock;

    //超时时间10s
    private static final int TIMEOUT = 10 * 1000;

    public void secKill(String productId) throws RuntimeException {
        long time = System.currentTimeMillis() + TIMEOUT;
        //加锁
        if (!redisLock.lock(productId, String.valueOf(time))) {
            throw new RuntimeException("人太多了，等会儿再试吧~");
        }

        //具体的秒杀逻辑

        //解锁
        redisLock.unlock(productId, String.valueOf(time));
    }
}