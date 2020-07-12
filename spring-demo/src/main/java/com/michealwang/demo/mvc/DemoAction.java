package com.michealwang.demo.mvc;

import com.michealwang.demo.service.IDemoService;
import com.michealwang.mvcframework.annotation.MKAutowired;
import com.michealwang.mvcframework.annotation.MKController;
import com.michealwang.mvcframework.annotation.MKRequestMapping;
import com.michealwang.mvcframework.annotation.MKRequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@MKController
@MKRequestMapping("/demo")
public class DemoAction {

    @MKAutowired
    private IDemoService demoService;
    private IDemoService demoService2;

    @MKRequestMapping("/query")
    public void query(HttpServletRequest request, HttpServletResponse response,
                      @MKRequestParam("name") String name,
                      @MKRequestParam("id") String id,
                      @MKRequestParam("addr") String addr) {
        String result = "My name is " + name + ",id = " + id + ", addr=" + addr;
        try {
            response.getWriter().write(result);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
