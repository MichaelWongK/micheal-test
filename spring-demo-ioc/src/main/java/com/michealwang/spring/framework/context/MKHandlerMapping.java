package com.michealwang.spring.framework.context;

import java.lang.reflect.Method;
import java.util.regex.Pattern;

public class MKHandlerMapping {

    private Object controller;     //保存方法对应的Controller实例对象
    private Method method;          //保存映射的方法
    private Pattern pattern;        //保存URL

    public MKHandlerMapping(Object controller, Method method, Pattern pattern) {
        this.controller = controller;
        this.method = method;
        this.pattern = pattern;
    }

    public Object getController() {
        return controller;
    }

    public void setController(Object controller) {
        this.controller = controller;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public Pattern getPattern() {
        return pattern;
    }

    public void setPattern(Pattern pattern) {
        this.pattern = pattern;
    }
}
