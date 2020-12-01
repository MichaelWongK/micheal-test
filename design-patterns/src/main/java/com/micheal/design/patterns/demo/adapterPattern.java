package com.micheal.design.patterns.demo;

/**
 * @author <a href="mailto:wangmk13@163.com">micheal.wang</a>
 * @date 2020/12/1 16:43
 * @Description
 * Convert the inface of a class into another interface clients expect.Adapter lets classes work together
 * that couldn't otherwise because of incompatible interface.
 * （将一个类的接口变换成客户端所期待的另一种接口，从而使原本因接口不匹配而无法在一起工作的两个类能够在一起工作。）
 * “系统的数据和行为都正确，单接口不符时，我们应该考虑使用适配器，目的是是控制范围之外的一个原有对象与某个接口匹配。
 * 适配器模式主要用于希望复用一些现存的类，但是接口又与复用环境不一致的情况。”
 */
interface A {
    void methodA();
}

interface C {
    void methodC();
}

class ACAdapter implements A,C {

    private C c;

    public ACAdapter(C c) {
        this.c = c;
    }

    @Override
    public void methodA() {
        c.methodC();
    }

    @Override
    public void methodC() {

    }
}

interface M {
    void methodM1();
    void methodM2();
    void methodM3();
}
interface N {
    void methodN1();
    void methodN2();
    void methodN3();
}

class MNAdapter implements M, N {


    @Override
    public void methodM1() {

    }

    @Override
    public void methodM2() {

    }

    @Override
    public void methodM3() {

    }

    @Override
    public void methodN1() {

    }

    @Override
    public void methodN2() {

    }

    @Override
    public void methodN3() {

    }
}

class MNImpl extends MNAdapter {
    @Override
    public void methodM1() {
        System.out.println("m1");
    }

    @Override
    public void methodN1() {
        System.out.println("n1");
    }
}

public class adapterPattern {

    /**
     * 适配对象
     */
    public static void ObjectAdapter() {
        C c = new C() {

            @Override
            public void methodC() {
                System.out.println("this is C method");
            }
        };
        A a = new ACAdapter(c);
        a.methodA();
    }

    public static void clazzAdapter() {
        N m = new MNImpl();
        m.methodN1();

    }

    public static void main(String[] args) {
        clazzAdapter();
    }
}
