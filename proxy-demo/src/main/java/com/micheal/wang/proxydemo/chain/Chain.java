package com.micheal.wang.proxydemo.chain;

import java.util.List;

public class Chain {

    private List<ChainHandler> handers;

    private int index = 4;

    public Chain(List<ChainHandler> handers) {
        this.handers = handers;
    }

    public void proceed() {
        System.out.println("handers.size:" + handers.size());
        if (index >= handers.size()) {
            return;
        }
        handers.get(index++).execute(this);
    }
}
