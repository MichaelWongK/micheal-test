package com.micheal.wang.proxydemo.dynamic;

import com.micheal.wang.proxydemo.pattern.Impl.RealSubject;
import com.micheal.wang.proxydemo.pattern.Subject;

import java.lang.reflect.Proxy;

public class Client {

    public static void main(String[] args) {
        System.getProperties().put("sun.misc.ProxyGenerator.saveGeneratedFiles", "true");
        Subject subject = (Subject) Proxy.newProxyInstance(Client.class.getClassLoader(), new Class[]{Subject.class}, new JdkProxySubject(new RealSubject()));
        subject.hello();
    }
}
