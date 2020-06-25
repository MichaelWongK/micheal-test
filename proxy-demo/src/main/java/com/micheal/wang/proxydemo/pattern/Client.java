package com.micheal.wang.proxydemo.pattern;

import com.micheal.wang.proxydemo.pattern.Impl.RealSubject;
import com.micheal.wang.proxydemo.pattern.peoxy.Proxy;

public class Client {

    public static void main(String[] args) {
        Subject subject = new Proxy(new RealSubject());
        subject.request();
    }
}
