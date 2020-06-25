package com.micheal.wang.proxydemo.cglib;

import com.micheal.wang.proxydemo.pattern.Impl.RealSubject;
import com.micheal.wang.proxydemo.pattern.Subject;
import net.sf.cglib.proxy.Enhancer;

public class Client {

    public static void main(String[] args) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(RealSubject.class);
        enhancer.setCallback(new DemoMethodInterceptor());
        Subject subject = (Subject) enhancer.create();
        subject.request();
    }
}
