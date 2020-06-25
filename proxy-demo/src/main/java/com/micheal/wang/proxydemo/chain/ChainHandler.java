package com.micheal.wang.proxydemo.chain;

public abstract class ChainHandler {

    public void execute(Chain chain) {
        System.out.println("chain:" + chain.toString());
        handleProcess();
        chain.proceed();
    }

    protected abstract void handleProcess();
}
