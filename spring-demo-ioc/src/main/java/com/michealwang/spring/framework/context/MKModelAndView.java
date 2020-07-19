package com.michealwang.spring.framework.context;

import java.util.Map;

public class MKModelAndView {

    private String viewName;
    private Map<String, ?> model;

    public MKModelAndView(String viewName) {
        this.viewName = viewName;
    }

    public MKModelAndView(String viewName, Map<String, Object> model) {
        this.viewName = viewName;
        this.model = model;
    }

    public String getViewName() {
        return this.viewName;
    }

    public Map<String, ?> getModel() {
        return this.model;
    }
}
