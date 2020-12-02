package com.micheal.design.patterns.demo.proxy.dynamicproxy;

/**
 * @author <a href="mailto:wangmk13@163.com">micheal.wang</a>
 * @date 2020/12/2 15:30
 * @Description
 */
public class IceCreamServiceImpl implements IceCreamService {

    public void makeIceCream(String fruit) {
        System.out.println("制作" + fruit + "🍦");
    }
}
