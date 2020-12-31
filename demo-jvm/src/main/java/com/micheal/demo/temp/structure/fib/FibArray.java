package com.micheal.demo.temp.structure.fib;

/**
 * @author <a href="mailto:wangmk13@163.com">micheal.wang</a>
 * @date 2020/12/30 20:16
 * @Description
 */
public class FibArray {

    // 0 1 1 2 3 5 8 13 21 34   ···   N

    public static int fib(int n) {
        if (n < 2) {
            return n;
        }
        return fib(n-1) + fib(n-2);
    }

    public static int fib2(int n) {
        int first = 0;
        int second = 1;
        for (int i = 0; i < n-1; i++) {
            int sum = first + second;
            first = second;
            second = sum;
        }
        return second;
    }

    public static void main(String[] args) {
        System.out.println(fib(7));
        System.out.println(fib2(7));
    }
}
