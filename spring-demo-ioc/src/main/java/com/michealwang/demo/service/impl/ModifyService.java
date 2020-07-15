package com.michealwang.demo.service.impl;

import com.michealwang.demo.service.IModifyService;
import com.michealwang.spring.framework.annotation.MKService;

@MKService
public class ModifyService implements IModifyService {
    public String add(String name, String addr) {
        return "My name is " + name + ", addr is " + addr;
    }
}
