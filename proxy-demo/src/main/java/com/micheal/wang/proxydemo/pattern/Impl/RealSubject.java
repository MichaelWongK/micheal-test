package com.micheal.wang.proxydemo.pattern.Impl;

import com.micheal.wang.proxydemo.pattern.Subject;

public class RealSubject implements Subject {
    @Override
    public void request() {
        System.out.println("real subject execute request");
    }

    @Override
    public void hello() {
        System.out.println("hello");
    }
}
