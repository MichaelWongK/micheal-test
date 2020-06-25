package com.micheal.wang.proxydemo.chain;

public class Client {
    static class HandlerA extends Handler {

        @Override
        protected void handleProcess() {
            System.out.println("handle by A");
        }
    }

    static class HandlerB extends Handler {

        @Override
        protected void handleProcess() {
            System.out.println("handle by B");
        }
    }

    static class HandlerC extends Handler {

        @Override
        protected void handleProcess() {
            System.out.println("handle by C");
        }
    }

    public static void main(String[] args) {
        Handler handlerA = new HandlerA();
        Handler handlerB = new HandlerB();
        Handler handlerC = new HandlerC();

        handlerA.setSucessor(handlerB);
        handlerB.setSucessor(handlerC);
        handlerA.execute();
    }
}
