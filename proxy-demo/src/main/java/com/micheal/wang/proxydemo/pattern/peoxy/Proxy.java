package com.micheal.wang.proxydemo.pattern.peoxy;

import com.micheal.wang.proxydemo.pattern.Impl.RealSubject;
import com.micheal.wang.proxydemo.pattern.Subject;

public class Proxy implements Subject {
    private RealSubject realSubject;

    public Proxy(RealSubject realSubject) {
        this.realSubject = realSubject;
    }

    @Override
    public void request() {
        System.out.println("before");
        try {
            realSubject.request();
        } catch (Exception e) {
            System.out.println("exception:" + e.getMessage());
            throw e;
        } finally {
            System.out.println("after:");
        }
    }

    @Override
    public void hello() {
        realSubject.hello();
    }
}
