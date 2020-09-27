package com.micheal.demo.temp.thread;

import org.junit.Test;

/**
 * @author <a href="mailto:wangmk13@163.com">micheal.wang</a>
 * @date 2020/9/16 20:20
 * @Description
 */
public class ThreadTest {
    @Test
    public void test1() {
        /**
         * first Thread-0 Thread-3 Thread-1 Thread-2 Thread-4
         * second Thread-1 Thread-2 Thread-3 Thread-0 Thread-4
         * 线程执行没有顺序。
         * 1.线程如何执行（JAVA中不涉及底层）
         *  1.1 Thread t1=new Thread(Runnable run);通过实现Runnable接口
         *  1.2 Thread t1=new Thread(){
         *  	@Override
         *  	public void run(){
         *  		xxx
         *  	}
         *  };
         *  通过继承Thread类覆写里面的run方法
         *  线程默认命名是：Thread-i
         */
        Thread t1=new Thread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                System.out.println(Thread.currentThread().getName());
            }
        });
        t1.setName("实现runnable");
        t1.start();
        Thread t2=new Thread() {
            @Override
            public void run() {
                System.out.println(Thread.currentThread().getName());
            }
        };
        //javascript:世界公认的慢慢的成为一线语言，React前端组件式编程
        //React Native这个可以写安卓和IOS VUE.JS这个是前端框架贼爽，
        //Node.js写后台的 才用单进程单线程 基于时间回调的语言 Redis线程模型跟这个一样
        //require('http').createServer(8080,function(){});
        //python是个好东西 为啥好  她就是一个粘合剂
        t2.setName("继承thread覆写run");
        t2.start();
    }
}
