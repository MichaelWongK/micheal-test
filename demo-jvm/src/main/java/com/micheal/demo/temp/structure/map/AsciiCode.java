package com.micheal.demo.temp.structure.map;

/**
 * @author <a href="mailto:wangmk13@163.com">micheal.wang</a>
 * @date 2021/1/6 15:40
 * @Description
 */
public class AsciiCode {

    public static void main(String[] args) {
        char c[] = "lies".toCharArray();
        for (int i = 0; i < c.length; i++) {
            System.out.println(c[i] + " : " + (int) c[i]);
        }
    }
}
