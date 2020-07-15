package com.michealwang.demo.action;

import com.michealwang.demo.service.IModifyService;
import com.michealwang.spring.framework.annotation.MKAutowired;
import com.michealwang.spring.framework.annotation.MKController;
import com.michealwang.spring.framework.annotation.MKRequestMapping;
import com.michealwang.spring.framework.annotation.MKRequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@MKController
@MKRequestMapping("/web/")
public class MyAction {

    @MKAutowired
    private IModifyService modifyService;

    @MKRequestMapping("/add")
    public void add(HttpServletRequest request, HttpServletResponse response,
                    @MKRequestParam("name") String name, @MKRequestParam("addr") String addr) {
        String result = modifyService.add(name, addr);
        out(response, result);
    }

    private void out(HttpServletResponse response, String result) {
        try {
            response.getWriter().write(result);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
