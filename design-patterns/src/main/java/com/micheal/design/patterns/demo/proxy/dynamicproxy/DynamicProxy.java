package com.micheal.design.patterns.demo.proxy.dynamicproxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author <a href="mailto:wangmk13@163.com">micheal.wang</a>
 * @date 2020/12/2 15:01
 * @Description jdk动态代理
 */
public class DynamicProxy  {

    // 代理的目标
    private Object target;

    public DynamicProxy(Object target) {
        this.target = target;
    }

    public Object getProxy() {
        Class<?> clazz = target.getClass();
        return Proxy.newProxyInstance(clazz.getClassLoader(), clazz.getInterfaces(), this::invoke);
    }

    /**
     * @param proxy  动态生成的代理对象
     * @param method 代理方法
     * @param args   代理方法的方法参数
     * @return 结果
     * @throws Throwable
     */
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        // 执行代理方法前执行方法
        beforeMethod(target);

        // 反射执行代理对象的目标方法
        Object result = method.invoke(target, args);

        // 执行代理方法结束后执行的方法
        afterMethod(target);

        return result;
    }

    private void beforeMethod(Object object) {
        if (object instanceof PieServcie) {
            System.out.println("准备派的材料");
        } else if (object instanceof IceCreamService) {
            System.out.println("准备冰淇淋材料");
        } else {
            throw new RuntimeException("暂不支持代理" + object.getClass() + "类型");
        }
    }

    private void afterMethod(Object object) {
        if (object instanceof PieServcie) {
            System.out.println("保鲜派");
        } else if (object instanceof IceCreamService) {
            System.out.println("保鲜冰淇淋");
        } else {
            throw new RuntimeException("暂不支持代理" + object.getClass() + "类型");
        }
    }
}
