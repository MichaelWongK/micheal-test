package com.michealwang.spring.framework.beans.support;

import com.michealwang.spring.framework.beans.config.MKBeanDefinition;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class MKBeanDefinitionReader {

    private Properties contextConfig = new Properties();

    // 存储所有类名
    private List<String> registryBeanClass = new ArrayList<String>();


    public MKBeanDefinitionReader(String... configLocations) {
        // 1.加载配置文件
        doLoadConfig(configLocations[0]);

        // 2.扫描相关的类
        doScanner(contextConfig.getProperty("scanPackage"));

    }

    public List<MKBeanDefinition> doLoadBeanDefinitions() {
        List<MKBeanDefinition> result = new ArrayList<MKBeanDefinition>();

        try {
            for (String className : registryBeanClass) {
                Class<?> beanClass = Class.forName(className);
                if (beanClass.isInterface()) {continue;}

                result.add(doCreateBeanDefinition(toLowerFirstCase(beanClass.getSimpleName()), beanClass.getName()));

                for (Class<?> i : beanClass.getInterfaces()) {
                    result.add(doCreateBeanDefinition(i.getName(), beanClass.getName()));
                }


            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


        return result;
    }

    private MKBeanDefinition doCreateBeanDefinition(String factoryBeanName, String beanClassName) {
        MKBeanDefinition beanDefinition = new MKBeanDefinition();
        beanDefinition.setFactoryBeanName(factoryBeanName);
        beanDefinition.setBeanClassName(beanClassName);


        return beanDefinition;
    }

    /**
     * 大写字母A对应ASCII码是65，加上32就是小写字母a对应ASCII码97
     * 同理：小写转大写字符-32即可
     * 如果已经是小写字母再加上32，超出字母对应ascii码值，输出" "空字符
     * @param simpleName
     * @return
     */
    private String toLowerFirstCase(String simpleName) {
        char[] chars = simpleName.toCharArray();
        chars[0] += 32;
        return String.valueOf(chars);
    }

    private void doScanner(String scanPackage) {

        URL url = this.getClass().getClassLoader().getResource("/" + scanPackage.replaceAll("\\.", "/"));
        File classParh = new File(url.getFile());

        for (File file : classParh.listFiles()) {

            if (file.isDirectory()) {
                doScanner(scanPackage + "." + file.getName());
            } else {
                // 取反，减少代码嵌套
                if (!file.getName().endsWith(".class")) {continue;}
                // 拼接全类名
                String className = scanPackage + "." + file.getName().replace(".class", "");
                registryBeanClass.add(className);

            }


        }

    }

    private void doLoadConfig(String contextConfigLocation) {
        // ClassPath 下去找到application.properties 文件并读取
        InputStream is = this.getClass().getClassLoader().getResourceAsStream(contextConfigLocation);

        try {
            contextConfig.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != is) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public Properties getConfig() {
        return this.contextConfig;
    }

}
