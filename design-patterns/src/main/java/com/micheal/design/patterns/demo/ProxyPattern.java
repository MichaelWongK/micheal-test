package com.micheal.design.patterns.demo;

/**
 * @author <a href="mailto:wangmk13@163.com">micheal.wang</a>
 * @date 2020/12/2 10:59
 * @Description 代理模式
 * Provide a surrogate (代理) or placeholder for another object to control access to it.
 * （为其他对象提供一种代理以控制对这个对象的访问。）
 */
class Station {
    private int ticketCount = 100;
    public void saleTicket() {
        ticketCount--;
        System.out.println("sale one ticket, ticket num: " + ticketCount);
    }
}

class Person {

    public void action(Station station) {
        station.saleTicket();
        System.out.println("buy success");
    }

    public void action(GoWhereAPP goWhereAPP) {
        goWhereAPP.saleTicket();
        System.out.println("gowhere buy success");
    }
}

/**
 * 静态代理
 */
class GoWhereAPP {
    private Station station = new Station();
    public void saleTicket() {
        System.out.println("扣掉一块钱");
        station.saleTicket();
    }
}

public class ProxyPattern {

    public static void main(String[] args) {
        Person person = new Person();
        person.action(new GoWhereAPP());
    }
}
