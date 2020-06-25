package com.micheal.wang.proxydemo.chain;

import java.util.Arrays;
import java.util.List;

public class ChainClient {
    static class ChainHandleA extends ChainHandler {

        @Override
        protected void handleProcess() {
            System.out.println("handle by Chain A");
        }
    }
    static class ChainHandleB extends ChainHandler {

        @Override
        protected void handleProcess() {
            System.out.println("handle by Chain B");
        }
    }
    static class ChainHandleC extends ChainHandler {

        @Override
        protected void handleProcess() {
            System.out.println("handle by Chain C");
        }
    }

    public static void main(String[] args) {
        List<ChainHandler> handlers = Arrays.asList(
                new ChainHandleA(),
                new ChainHandleB(),
                new ChainHandleC()
        );
        Chain chain = new Chain(handlers);
        chain.proceed();
    }
}
