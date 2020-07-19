package com.michealwang.spring.framework.context;

import com.michealwang.spring.framework.annotation.MKAutowired;
import com.michealwang.spring.framework.annotation.MKController;
import com.michealwang.spring.framework.annotation.MKService;
import com.michealwang.spring.framework.beans.MKBeanWrapper;
import com.michealwang.spring.framework.beans.config.MKBeanDefinition;
import com.michealwang.spring.framework.beans.support.MKBeanDefinitionReader;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class MKApplicationContext {

    private  String[] configLocations;

    private MKBeanDefinitionReader reader;

    private Map<String, MKBeanDefinition> beanDefinitionMap = new HashMap<String, MKBeanDefinition>();

    private Map<String, MKBeanWrapper> factoryBeanInstanceCache = new HashMap<String, MKBeanWrapper>();

    private Map<String, Object> factoryBeanObjectCache = new HashMap<String, Object>();



    public MKApplicationContext(String ... configLocations) {
        this.configLocations = configLocations;

        try {
            // 1.读取配置文件, 并且封装成BeanDefinition对象
            reader = new MKBeanDefinitionReader(this.configLocations);
            List<MKBeanDefinition> beanDefinitions = reader.doLoadBeanDefinitions();

            // 2.将beanDefinition对象缓存到beanDefinitionMap中
            doRegistyBeanDefinition(beanDefinitions);

            // 3.触发对象实例化的动作
            doCreateBean();

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void doCreateBean() {
        for (Map.Entry<String, MKBeanDefinition> beanDefinitionEntry : this.beanDefinitionMap.entrySet()) {
            String beanName = beanDefinitionEntry.getKey();
            // 1.真正完成创建动作
            // 2.完成DI
            getBean(beanName);
        }

    }

    private void doRegistyBeanDefinition(List<MKBeanDefinition> beanDefinitions) throws Exception {
        for (MKBeanDefinition beanDefinition : beanDefinitions) {
            if(this.beanDefinitionMap.containsKey(beanDefinition.getFactoryBeanName())) {
                throw new Exception("The " + beanDefinition.getFactoryBeanName() + " is exists!!");
            }
            this.beanDefinitionMap.put(beanDefinition.getFactoryBeanName(), beanDefinition);
            this.beanDefinitionMap.put(beanDefinition.getBeanClassName(), beanDefinition);

        }


    }

    public Object getBean(String beanName) {

        // 1. 拿到beanName对应的配置信息BeanDefinition
        MKBeanDefinition beanDefinition = this.beanDefinitionMap.get(beanName);

        // 2. 实例化对象
        Object instance = instaniate(beanName, beanDefinition);

        // 3. 将实例化的对象封装成BeanWrapper
        MKBeanWrapper beanWrapper = new MKBeanWrapper(instance);

        // 4. 将BeanWrapper缓存到spring容器中
        factoryBeanInstanceCache.put(beanName, beanWrapper);

        // 5. 完成DI
        populateBean(beanName, beanDefinition, beanWrapper);


        return this.factoryBeanInstanceCache.get(beanName).getWrapperInstance();
    }

    private void populateBean(String beanName, MKBeanDefinition beanDefinition, MKBeanWrapper beanWrapper) {

        Object instance = beanWrapper.getWrapperInstance();
        Class<?> clazz = beanWrapper.getWrapperClass();

        if (!(clazz.isAnnotationPresent(MKController.class) || clazz.isAnnotationPresent(MKService.class))) {
            return;
        }

        // private /public /private /protected
        for (Field field : clazz.getDeclaredFields()) {
            if (!field.isAnnotationPresent(MKAutowired.class)) {
                continue;
            }

            MKAutowired autowired = field.getAnnotation(MKAutowired.class);
            String autowiredBeanName = autowired.value().trim();
            if ("".equals(autowiredBeanName)) {
                autowiredBeanName = field.getType().getName();
            }

            // 暴力访问
            field.setAccessible(true);

            try {
                if (this.factoryBeanInstanceCache.get(autowiredBeanName) == null) {continue;}
                field.set(instance, this.factoryBeanInstanceCache.get(autowiredBeanName).getWrapperInstance());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                continue;
            }
        }


    }

    private Object instaniate(String beanName, MKBeanDefinition beanDefinition) {
        String className = beanDefinition.getBeanClassName();
        Object instance = null;
        try {
            Class<?> clazz = Class.forName(className);
            instance = clazz.newInstance();

            // 如果这个instance对象要生成代理，此处就是预留入口


            this.factoryBeanObjectCache.put(beanName, instance);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return instance;
    }


    public Object getBean(Class className) {
        return getBean(className.getName());
    }

    public int getBeanDefinitionCount() {
        return this.beanDefinitionMap.size();
    }

    public String[] getBeanDefinitionNames() {
        return this.beanDefinitionMap.keySet().toArray(new String[this.beanDefinitionMap.size()]);
    }

    public Properties getConfig() {
        return this.reader.getConfig();
    }
}
