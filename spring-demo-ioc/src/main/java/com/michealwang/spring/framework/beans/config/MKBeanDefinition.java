package com.michealwang.spring.framework.beans.config;

public class MKBeanDefinition  {

    private String factoryBeanName;

    private String beanClassName;

    public String getBeanClassName() {
        return beanClassName;
    }

    public void setBeanClassName(String beanClassName) {
        this.beanClassName = beanClassName;
    }

    public void setFactoryBeanName(String factoryBeanName) {
        this.factoryBeanName = factoryBeanName;
    }

    public String getFactoryBeanName() {
        return factoryBeanName;
    }
}
