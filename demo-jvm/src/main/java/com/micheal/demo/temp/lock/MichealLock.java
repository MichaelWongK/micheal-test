package com.micheal.demo.temp.lock;

import org.apache.tomcat.jni.Error;
import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author <a href="mailto:wangmk13@163.com">micheal.wang</a>
 * @date 2020/12/15 23:41
 * @Description
 */
public class MichealLock {
    private static  sun.misc.Unsafe unsafe;
//    private static  Unsafe unsafe;
    private static  long valueOffset;

    static {
        try {
            Class<Unsafe> unsafeClass = Unsafe.class;
            Field theUnsafe = unsafeClass.getDeclaredField("theUnsafe");
            theUnsafe.setAccessible(true);
            unsafe = sun.misc.Unsafe.getUnsafe();
            valueOffset = unsafe.objectFieldOffset
                    (AtomicInteger.class.getDeclaredField("value"));
        } catch (Exception e) {
//            throw new Error();
        }
    }

    private volatile int value = 0;

    public void lock() {
        for ( ; ; ) {
            if (unsafe.compareAndSwapInt(this, valueOffset, 0, 1));
            return;
        }
    }

    public void unlock() {
        value = 0;
    }
}
