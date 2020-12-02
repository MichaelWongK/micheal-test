package com.micheal.design.patterns.demo.proxy.dynamicproxy;

/**
 * @author <a href="mailto:wangmk13@163.com">micheal.wang</a>
 * @date 2020/12/2 15:33
 * @Description
 */
public class Application {

    public static void main(String[] args) {

        PieServcie pieServiceDynamicProxy = (PieServcie) new DynamicProxy(new PieServiceImpl()).getProxy();
        pieServiceDynamicProxy.makePie();
        System.out.println("-----------------");
        IceCreamService iceCreamServiceDynamicProxy = (IceCreamService) new DynamicProxy(new IceCreamServiceImpl()).getProxy();
        iceCreamServiceDynamicProxy.makeIceCream("🍓");
    }
}
