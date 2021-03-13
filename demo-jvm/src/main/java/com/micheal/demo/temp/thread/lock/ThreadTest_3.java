package com.micheal.demo.temp.thread.lock;

import org.openjdk.jol.info.ClassLayout;

/**
 * @author <a href="mailto:wangmk13@163.com">micheal.wang</a>
 * @date 2021/3/3 21:48
 * @Description
 */
public class ThreadTest_3 {

    static int val = 0;

    public static void main(String[] args) {
        Object obj = new Object();
        System.out.println(ClassLayout.parseInstance(obj).toPrintable());
    }

    // .out
    //java.lang.Object object internals:
    // OFFSET  SIZE   TYPE DESCRIPTION                               VALUE
    //      0     4        (object header)                           01 00 00 00 (00000001 00000000 00000000 00000000) (1)
    //      4     4        (object header)                           00 00 00 00 (00000000 00000000 00000000 00000000) (0)
    //      8     4        (object header)                           e5 01 00 f8 (11100101 00000001 00000000 11111000) (-134217243)
    //     12     4        (loss due to the next object alignment)
    //Instance size: 16 bytes
    //Space losses: 0 bytes internal + 4 bytes external = 4 bytes total
}
