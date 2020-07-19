package com.michealwang.demo.action;

import com.michealwang.demo.service.IQueryService;
import com.michealwang.spring.framework.annotation.MKAutowired;
import com.michealwang.spring.framework.annotation.MKController;
import com.michealwang.spring.framework.annotation.MKRequestMapping;
import com.michealwang.spring.framework.annotation.MKRequestParam;
import com.michealwang.spring.framework.context.MKModelAndView;
//import com.gupaoedu.vip.spring.framework.webmvc.servlet.GPModelAndView;

import java.util.HashMap;
import java.util.Map;

/**
 * 公布接口url
 * @author Tom
 *
 */
@MKController
@MKRequestMapping("/")
public class PageAction {

    @MKAutowired
    private IQueryService queryService;

    @MKRequestMapping("/first.html")
    public MKModelAndView query(@MKRequestParam("name") String name){
        String result = queryService.query(name);
        Map<String,Object> model = new HashMap<String,Object>();
        model.put("name", name);
        model.put("data", result);
        model.put("token", "123456");
        return new MKModelAndView("first.html",model);
    }

}
