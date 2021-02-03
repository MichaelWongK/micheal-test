package com.micheal.demo.temp.jvm.heap;

import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:wangmk13@163.com">micheal.wang</a>
 * @date 2021/1/6 23:23
 * @Description
 */
public class HeapOom {

    byte[] b = new byte[1024 * 100];


    public static void main(String[] args) throws InterruptedException {
        List<HeapOom> all = new ArrayList<>();
        while (true) {
            all.add(new HeapOom());
            Thread.sleep(10);
        }
    }
}
