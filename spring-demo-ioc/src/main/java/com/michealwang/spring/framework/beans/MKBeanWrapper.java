package com.michealwang.spring.framework.beans;

public class MKBeanWrapper {


    private Object wrapperInstance;

    private Class<?> wrapperClass;

    public MKBeanWrapper(Object wrapperInstance) {
        this.wrapperInstance = wrapperInstance;
        this.wrapperClass = wrapperInstance.getClass();
    }

    public Object getWrapperInstance() {
        return wrapperInstance;
    }

    public Class<?> getWrapperClass() {
        return wrapperClass;
    }
}
