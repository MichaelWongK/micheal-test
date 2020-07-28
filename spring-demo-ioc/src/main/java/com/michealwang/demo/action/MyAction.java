package com.michealwang.demo.action;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.michealwang.demo.service.IModifyService;
import com.michealwang.demo.service.IQueryService;
import com.michealwang.spring.framework.annotation.MKAutowired;
import com.michealwang.spring.framework.annotation.MKController;
import com.michealwang.spring.framework.annotation.MKRequestMapping;
import com.michealwang.spring.framework.annotation.MKRequestParam;
import com.michealwang.spring.framework.context.MKModelAndView;

/**
 * 公布接口url
 *
 * @author micheal
 */
@MKController
@MKRequestMapping("/web")
public class MyAction {

    @MKAutowired
    IQueryService queryService;
    @MKAutowired
    IModifyService modifyService;

    @MKRequestMapping("/query.json")
    public MKModelAndView query(HttpServletRequest request, HttpServletResponse response,
                                @MKRequestParam("name") String name) {
        String result = queryService.query(name);
        return out(response, result);
    }

    @MKRequestMapping("/add*.json")
    public MKModelAndView add(HttpServletRequest request, HttpServletResponse response,
                              @MKRequestParam("name") String name, @MKRequestParam("addr") String addr) {
        String result = modifyService.add(name, addr);
        return out(response, result);
    }

    @MKRequestMapping("/remove.json")
    public MKModelAndView remove(HttpServletRequest request, HttpServletResponse response,
                                 @MKRequestParam("id") Integer id) {
        String result = modifyService.remove(id);
        return out(response, result);
    }

    @MKRequestMapping("/edit.json")
    public MKModelAndView edit(HttpServletRequest request, HttpServletResponse response,
                               @MKRequestParam("id") Integer id,
                               @MKRequestParam("name") String name) {
        String result = modifyService.edit(id, name);
        return out(response, result);
    }


    private MKModelAndView out(HttpServletResponse resp, String str) {
        try {
            resp.getWriter().write(str);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
